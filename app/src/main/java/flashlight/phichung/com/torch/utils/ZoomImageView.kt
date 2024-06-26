package flashlight.phichung.com.torch.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.OverScroller
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

class ZoomImageView : AppCompatImageView {
    /**
     * Get the current zoom. This is the zoom relative to the initial
     * scale, not the original resource.
     *
     * @return current zoom multiplier.
     */
    //
    // Scale of image ranges from minScale to maxScale, where minScale == 1
    // when the image is stretched to fit view.
    //
    var currentZoom = 0f
        private set

    //
    // Matrix applied to image. MSCALE_X and MSCALE_Y should always be equal.
    // MTRANS_X and MTRANS_Y are the other values used. prevMatrix is the matrix
    // saved prior to the screen rotating.
    //
    private var matrixM: Matrix? = null
    private var prevMatrix: Matrix? = null
    private var state: State? = null
    private var minScale = 0f
    private var maxScale = 0f
    private var superMinScale = 0f
    private var superMaxScale = 0f
    private var m: FloatArray? = null
    private var contextM: Context? = null
    private var fling: Fling? = null
    private var mScaleType: ScaleType? = null
    private var imageRenderedAtLeastOnce = false
    private var onDrawReady = false
    private var delayedZoomVariables: ZoomVariables? = null

    //
    // Size of view and previous view size (ie before rotation)
    //
    private var viewWidth = 0
    private var viewHeight = 0
    private var prevViewWidth = 0
    private var prevViewHeight = 0

    //
    // Size of image when it is stretched to fit view. Before and After rotation.
    //
    private var matchViewWidth = 0f
    private var matchViewHeight = 0f
    private var prevMatchViewWidth = 0f
    private var prevMatchViewHeight = 0f
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null
    private var doubleTapListener: GestureDetector.OnDoubleTapListener? = null
    private var userTouchListener: OnTouchListener? = null
    private var touchImageViewListener: OnZoomImageViewListener? = null

    constructor(context: Context) : super(context) {
        sharedConstructing(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(
        context,
        attrs
    ) {
        sharedConstructing(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(
        context,
        attrs,
        defStyle
    ) {
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        this.contextM = context
        mScaleDetector = ScaleGestureDetector(
            context,
            ScaleListener()
        )
        mGestureDetector = GestureDetector(
            context,
            GestureListener()
        )
        matrixM = Matrix()
        prevMatrix = Matrix()
        m = FloatArray(9)
        currentZoom = 1f
        if (mScaleType == null) {
            mScaleType = ScaleType.FIT_CENTER
        }
        minScale = 1f
        maxScale = 3f
        superMinScale = SUPER_MIN_MULTIPLIER * minScale
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale
        imageMatrix = matrixM
        scaleType = ScaleType.MATRIX
        setState(State.NONE)
        onDrawReady = false
        super.setOnTouchListener(PrivateOnTouchListener())
    }


    override fun setOnTouchListener(l: OnTouchListener) {
        userTouchListener = l
    }

    fun setOnZoomImageViewListener(l: OnZoomImageViewListener?) {
        touchImageViewListener = l
    }

    fun setOnDoubleTapListener(l: GestureDetector.OnDoubleTapListener?) {
        doubleTapListener = l
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun getScaleType(): ScaleType {
        return mScaleType!!
    }

    override fun setScaleType(type: ScaleType) {
        if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
            throw UnsupportedOperationException("ZoomImageView does not support FIT_START or FIT_END")
        }
        if (type == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX)
        } else {
            mScaleType = type
            if (onDrawReady) {
                //
                // If the image is already rendered, scaleType has been called programmatically
                // and the ZoomImageView should be updated with the new scaleType.
                //
                setZoom(this)
            }
        }
    }

    /**
     * Returns false if image is in initial, unzoomed state. False, otherwise.
     *
     * @return true if image is zoomed
     */
    val isZoomed: Boolean
        get() = currentZoom != 1f

    /**
     * Return a Rect representing the zoomed image.
     *
     * @return rect representing zoomed image
     */
    val zoomedRect: RectF
        get() {
            if (mScaleType == ScaleType.FIT_XY) {
                throw UnsupportedOperationException("getZoomedRect() not supported with FIT_XY")
            }
            val topLeft = transformCoordinatesTouchToBitmap(
                0f,
                0f,
                true
            )
            val bottomRight = transformCoordinatesTouchToBitmap(
                viewWidth.toFloat(),
                viewHeight.toFloat(),
                true
            )
            val w = drawable.intrinsicWidth.toFloat()
            val h = drawable.intrinsicHeight.toFloat()
            return RectF(
                topLeft.x / w,
                topLeft.y / h,
                bottomRight.x / w,
                bottomRight.y / h
            )
        }

    /**
     * Save the current matrix and view dimensions
     * in the prevMatrix and prevView variables.
     */
    private fun savePreviousImageValues() {
        if (matrixM != null && viewHeight != 0 && viewWidth != 0) {
            matrixM!!.getValues(m)
            prevMatrix!!.setValues(m)
            prevMatchViewHeight = matchViewHeight
            prevMatchViewWidth = matchViewWidth
            prevViewHeight = viewHeight
            prevViewWidth = viewWidth
        }
    }


    public override fun onSaveInstanceState(): Parcelable {
        super.onSaveInstanceState()
        val bundle = Bundle()
//        bundle.putParcelable(
//            "instanceState",
//            super.onSaveInstanceState()
//        )
        bundle.putFloat(
            "saveScale",
            currentZoom
        )
        bundle.putFloat(
            "matchViewHeight",
            matchViewHeight
        )
        bundle.putFloat(
            "matchViewWidth",
            matchViewWidth
        )
        bundle.putInt(
            "viewWidth",
            viewWidth
        )
        bundle.putInt(
            "viewHeight",
            viewHeight
        )
        matrixM!!.getValues(m)
        bundle.putFloatArray(
            "matrix",
            m
        )
        bundle.putBoolean(
            "imageRendered",
            imageRenderedAtLeastOnce
        )
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {

        if (state is Bundle) {
            val bundle = state
            currentZoom = bundle.getFloat("saveScale")
            m = bundle.getFloatArray("matrix")
            prevMatrix!!.setValues(m)
            prevMatchViewHeight = bundle.getFloat("matchViewHeight")
            prevMatchViewWidth = bundle.getFloat("matchViewWidth")
            prevViewHeight = bundle.getInt("viewHeight")
            prevViewWidth = bundle.getInt("viewWidth")
            imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered")

        }
        return super.onRestoreInstanceState(state)

    }

    override fun onDraw(canvas: Canvas) {
        onDrawReady = true
        imageRenderedAtLeastOnce = true
        if (delayedZoomVariables != null) {
            setZoom(
                delayedZoomVariables!!.scale,
                delayedZoomVariables!!.focusX,
                delayedZoomVariables!!.focusY,
                delayedZoomVariables!!.scaleType
            )
            delayedZoomVariables = null
        }
        super.onDraw(canvas)
    }

    public override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        savePreviousImageValues()
    }
    /**
     * Get the max zoom multiplier.
     *
     * @return max zoom multiplier.
     */
    /**
     * Set the max zoom multiplier. Default value: 3.
     *
     * @param max max zoom multiplier.
     */
    var maxZoom: Float
        get() = maxScale
        set(max) {
            maxScale = max
            superMaxScale = SUPER_MAX_MULTIPLIER * maxScale
        }
    /**
     * Get the min zoom multiplier.
     *
     * @return min zoom multiplier.
     */
    /**
     * Set the min zoom multiplier. Default value: 1.
     *
     * @param min min zoom multiplier.
     */
    var minZoom: Float
        get() = minScale
        set(min) {
            minScale = min
            superMinScale = SUPER_MIN_MULTIPLIER * minScale
        }

    /**
     * Reset zoom and translation to initial state.
     */
    fun resetZoom() {
        currentZoom = 1f
        fitImageToView()
    }

    /**
     * Set zoom to the specified scale. Image will be centered by default.
     *
     * @param scale
     */
    fun setZoom(scale: Float) {
        setZoom(
            scale,
            0.5f,
            0.5f
        )
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     *
     * @param scale
     * @param focusX
     * @param focusY
     */
    fun setZoom(
        scale: Float,
        focusX: Float,
        focusY: Float
    ) {
        setZoom(
            scale,
            focusX,
            focusY,
            mScaleType
        )
    }

    /**
     * Set zoom to the specified scale. Image will be centered around the point
     * (focusX, focusY). These floats range from 0 to 1 and denote the focus point
     * as a fraction from the left and top of the view. For example, the top left
     * corner of the image would be (0, 0). And the bottom right corner would be (1, 1).
     *
     * @param scale
     * @param focusX
     * @param focusY
     * @param scaleType
     */
    fun setZoom(
        scale: Float,
        focusX: Float,
        focusY: Float,
        scaleType: ScaleType?
    ) {
        //
        // setZoom can be called before the image is on the screen, but at this point,
        // image and view sizes have not yet been calculated in onMeasure. Thus, we should
        // delay calling setZoom until the view has been measured.
        //
        if (!onDrawReady) {
            delayedZoomVariables = ZoomVariables(
                scale,
                focusX,
                focusY,
                scaleType!!
            )
            return
        }
        if (scaleType != mScaleType) {
            setScaleType(scaleType!!)
        }
        resetZoom()
        scaleImage(
            scale.toDouble(),
            (
                    viewWidth / 2).toFloat(),
            (
                    viewHeight / 2).toFloat(),
            true
        )
        matrixM!!.getValues(m)
        m!![Matrix.MTRANS_X] = -(focusX * imageWidth - viewWidth * 0.5f)
        m!![Matrix.MTRANS_Y] = -(focusY * imageHeight - viewHeight * 0.5f)
        matrixM!!.setValues(m)
        fixTrans()
        imageMatrix = matrixM
    }

    /**
     * Set zoom parameters equal to another ZoomImageView. Including scale, position,
     * and ScaleType.
     *
     * @param
     */
    fun setZoom(img: ZoomImageView) {
        val center = img.scrollPosition
        setZoom(
            img.currentZoom,
            center!!.x,
            center.y,
            img.scaleType
        )
    }

    /**
     * Return the point at the center of the zoomed image. The PointF coordinates range
     * in value between 0 and 1 and the focus point is denoted as a fraction from the left
     * and top of the view. For example, the top left corner of the image would be (0, 0).
     * And the bottom right corner would be (1, 1).
     *
     * @return PointF representing the scroll position of the zoomed image.
     */
    val scrollPosition: PointF?
        get() {
            val drawable = drawable ?: return null
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight
            val point = transformCoordinatesTouchToBitmap(
                (viewWidth / 2).toFloat(),
                (
                        viewHeight / 2).toFloat(),
                true
            )
            point.x /= drawableWidth.toFloat()
            point.y /= drawableHeight.toFloat()
            return point
        }

    /**
     * Set the focus point of the zoomed image. The focus points are denoted as a fraction from the
     * left and top of the view. The focus points can range in value between 0 and 1.
     *
     * @param focusX
     * @param focusY
     */
    fun setScrollPosition(
        focusX: Float,
        focusY: Float
    ) {
        setZoom(
            currentZoom,
            focusX,
            focusY
        )
    }

    /**
     * Performs boundary checking and fixes the image matrix if it
     * is out of bounds.
     */
    private fun fixTrans() {
        matrixM!!.getValues(m)
        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]
        val fixTransX = getFixTrans(
            transX,
            viewWidth.toFloat(),
            imageWidth
        )
        val fixTransY = getFixTrans(
            transY,
            viewHeight.toFloat(),
            imageHeight
        )
        if (fixTransX != 0f || fixTransY != 0f) {
            matrixM!!.postTranslate(
                fixTransX,
                fixTransY
            )
        }
    }

    /**
     * When transitioning from zooming from focus to zoom from center (or vice versa)
     * the image can become unaligned within the view. This is apparent when zooming
     * quickly. When the content size is less than the view size, the content will often
     * be centered incorrectly within the view. fixScaleTrans first calls fixTrans() and
     * then makes sure the image is centered correctly within the view.
     */
    private fun fixScaleTrans() {
        fixTrans()
        matrixM!!.getValues(m)
        if (imageWidth < viewWidth) {
            m!![Matrix.MTRANS_X] = (viewWidth - imageWidth) / 2
        }
        if (imageHeight < viewHeight) {
            m!![Matrix.MTRANS_Y] = (viewHeight - imageHeight) / 2
        }
        matrixM!!.setValues(m)
    }

    private fun getFixTrans(
        trans: Float,
        viewSize: Float,
        contentSize: Float
    ): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) {
            return -trans + minTrans
        }
        return if (trans > maxTrans) {
            -trans + maxTrans
        } else 0f
    }

    private fun getFixDragTrans(
        delta: Float,
        viewSize: Float,
        contentSize: Float
    ): Float {
        return if (contentSize <= viewSize) {
            0f
        } else delta
    }

    private val imageWidth: Float
        get() = matchViewWidth * currentZoom
    private val imageHeight: Float
        get() = matchViewHeight * currentZoom

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val drawable = drawable
        if ((drawable == null) || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            setMeasuredDimension(
                0,
                0
            )
            return
        }
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        viewWidth = setViewSize(
            widthMode,
            widthSize,
            drawableWidth
        )
        viewHeight = setViewSize(
            heightMode,
            heightSize,
            drawableHeight
        )

        //
        // Set view dimensions
        //
        setMeasuredDimension(
            viewWidth,
            viewHeight
        )

        //
        // Fit content within view
        //
        fitImageToView()
    }

    /**
     * If the normalizedScale is equal to 1, then the image is made to fit the screen. Otherwise,
     * it is made to fit the screen according to the dimensions of the previous image matrix. This
     * allows the image to maintain its zoom after rotation.
     */
    private fun fitImageToView() {
        val drawable = drawable
        if ((drawable == null) || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            return
        }
        if (matrixM == null || prevMatrix == null) {
            return
        }
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight

        //
        // Scale image for view
        //
        var scaleX = viewWidth.toFloat() / drawableWidth
        var scaleY = viewHeight.toFloat() / drawableHeight
        when (mScaleType) {
            ScaleType.CENTER -> {
                scaleY = 1f
                scaleX = scaleY
            }

            ScaleType.CENTER_CROP -> {
                scaleY = scaleX.coerceAtLeast(scaleY)
                scaleX = scaleY
            }

            ScaleType.CENTER_INSIDE -> {
                run {
                    scaleY = 1f.coerceAtMost(
                        Math.min(
                            scaleX,
                            scaleY
                        )
                    )
                    scaleX = scaleY
                }
                run {
                    scaleY = scaleX.coerceAtMost(scaleY)
                    scaleX = scaleY
                }
            }

            ScaleType.FIT_CENTER -> {
                scaleY = scaleX.coerceAtMost(scaleY)
                scaleX = scaleY
            }

            ScaleType.FIT_XY -> {}
            else -> throw UnsupportedOperationException("ZoomImageView does not support FIT_START or FIT_END")
        }

        //
        // Center the image
        //
        val redundantXSpace = viewWidth - scaleX * drawableWidth
        val redundantYSpace = viewHeight - scaleY * drawableHeight
        matchViewWidth = viewWidth - redundantXSpace
        matchViewHeight = viewHeight - redundantYSpace
        if (!isZoomed && !imageRenderedAtLeastOnce) {
            //
            // Stretch and center image to fit view
            //
            matrixM!!.setScale(
                scaleX,
                scaleY
            )
            matrixM!!.postTranslate(
                redundantXSpace / 2,
                redundantYSpace / 2
            )
            currentZoom = 1f
        } else {
            //
            // These values should never be 0 or we will set viewWidth and viewHeight
            // to NaN in translateMatrixAfterRotate. To avoid this, call savePreviousImageValues
            // to set them equal to the current values.
            //
            if (prevMatchViewWidth == 0f || prevMatchViewHeight == 0f) {
                savePreviousImageValues()
            }
            prevMatrix!!.getValues(m)

            //
            // Rescale Matrix after rotation
            //
            m!![Matrix.MSCALE_X] = matchViewWidth / drawableWidth * currentZoom
            m!![Matrix.MSCALE_Y] = matchViewHeight / drawableHeight * currentZoom

            //
            // TransX and TransY from previous matrix
            //
            val transX = m!![Matrix.MTRANS_X]
            val transY = m!![Matrix.MTRANS_Y]

            //
            // Width
            //
            val prevActualWidth = prevMatchViewWidth * currentZoom
            val actualWidth = imageWidth
            translateMatrixAfterRotate(
                Matrix.MTRANS_X,
                transX,
                prevActualWidth,
                actualWidth,
                prevViewWidth,
                viewWidth,
                drawableWidth
            )

            //
            // Height
            //
            val prevActualHeight = prevMatchViewHeight * currentZoom
            val actualHeight = imageHeight
            translateMatrixAfterRotate(
                Matrix.MTRANS_Y,
                transY,
                prevActualHeight,
                actualHeight,
                prevViewHeight,
                viewHeight,
                drawableHeight
            )

            //
            // Set the matrix to the adjusted scale and translate values.
            //
            matrixM!!.setValues(m)
        }
        fixTrans()
        imageMatrix = matrixM
    }

    /**
     * Set view dimensions based on layout params
     *
     * @param mode
     * @param size
     * @param drawableWidth
     * @return
     */
    private fun setViewSize(
        mode: Int,
        size: Int,
        drawableWidth: Int
    ): Int {
        val viewSize: Int
        viewSize = when (mode) {
            MeasureSpec.EXACTLY -> size
            MeasureSpec.AT_MOST -> Math.min(
                drawableWidth,
                size
            )

            MeasureSpec.UNSPECIFIED -> drawableWidth
            else -> size
        }
        return viewSize
    }

    /**
     * After rotating, the matrix needs to be translated. This function finds the area of image
     * which was previously centered and adjusts translations so that is again the center,
     * post-rotation.
     *
     * @param axis          Matrix.MTRANS_X or Matrix.MTRANS_Y
     * @param trans         the value of trans in that axis before the rotation
     * @param prevImageSize the width/height of the image before the rotation
     * @param imageSize     width/height of the image after rotation
     * @param prevViewSize  width/height of view before rotation
     * @param viewSize      width/height of view after rotation
     * @param drawableSize  width/height of drawable
     */
    private fun translateMatrixAfterRotate(
        axis: Int,
        trans: Float,
        prevImageSize: Float,
        imageSize: Float,
        prevViewSize: Int,
        viewSize: Int,
        drawableSize: Int
    ) {
        if (imageSize < viewSize) {
            //
            // The width/height of image is less than the view's width/height. Center it.
            //
            m!![axis] = (viewSize - drawableSize * m!![Matrix.MSCALE_X]) * 0.5f
        } else if (trans > 0) {
            //
            // The image is larger than the view, but was not before rotation. Center it.
            //
            m!![axis] = -((imageSize - viewSize) * 0.5f)
        } else {
            //
            // Find the area of the image which was previously centered in the view. Determine its distance
            // from the left/top side of the view as a fraction of the entire image's width/height. Use that percentage
            // to calculate the trans in the new view width/height.
            //
            val percentage = (abs(trans) + 0.5f * prevViewSize) / prevImageSize
            m!![axis] = -(percentage * imageSize - viewSize * 0.5f)
        }
    }

    private fun setState(state: State) {
        this.state = state
    }

    fun canScrollHorizontallyFroyo(direction: Int): Boolean {
        return canScrollHorizontally(direction)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        matrixM!!.getValues(m)
        val x = m!![Matrix.MTRANS_X]
        return if (imageWidth < viewWidth) {
            false
        } else if (x >= -1 && direction < 0) {
            false
        } else abs(x) + viewWidth + 1 < imageWidth || direction <= 0
    }

    private fun scaleImage(
        deltaScale: Double,
        focusX: Float,
        focusY: Float,
        stretchImageToSuper: Boolean
    ) {
        var deltaScaleImage = deltaScale
        val lowerScale: Float
        val upperScale: Float
        if (stretchImageToSuper) {
            lowerScale = superMinScale
            upperScale = superMaxScale
        } else {
            lowerScale = minScale
            upperScale = maxScale
        }
        val origScale = currentZoom
        currentZoom *= deltaScaleImage.toFloat()
        if (currentZoom > upperScale) {
            currentZoom = upperScale
            deltaScaleImage = (upperScale / origScale).toDouble()
        } else if (currentZoom < lowerScale) {
            currentZoom = lowerScale
            deltaScaleImage = (lowerScale / origScale).toDouble()
        }
        matrixM!!.postScale(
            deltaScaleImage.toFloat(), deltaScaleImage.toFloat(),
            focusX,
            focusY
        )
        fixScaleTrans()
    }

    /**
     * This function will transform the coordinates in the touch event to the coordinate
     * system of the drawable that the imageview contain
     *
     * @param x            x-coordinate of touch event
     * @param y            y-coordinate of touch event
     * @param clipToBitmap Touch event may occur within view, but outside image content. True, to clip return
     * value
     * to the bounds of the bitmap size.
     * @return Coordinates of the point touched, in the coordinate system of the original drawable.
     */
    private fun transformCoordinatesTouchToBitmap(
        x: Float,
        y: Float,
        clipToBitmap: Boolean
    ): PointF {
        matrixM!!.getValues(m)
        val origW = drawable.intrinsicWidth.toFloat()
        val origH = drawable.intrinsicHeight.toFloat()
        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]
        var finalX = (x - transX) * origW / imageWidth
        var finalY = (y - transY) * origH / imageHeight
        if (clipToBitmap) {
            finalX = Math.min(
                Math.max(
                    finalX,
                    0f
                ),
                origW
            )
            finalY = Math.min(
                Math.max(
                    finalY,
                    0f
                ),
                origH
            )
        }
        return PointF(
            finalX,
            finalY
        )
    }

    /**
     * Inverse of transformCoordTouchToBitmap. This function will transform the coordinates in the
     * drawable's coordinate system to the view's coordinate system.
     *
     * @param bx x-coordinate in original bitmap coordinate system
     * @param by y-coordinate in original bitmap coordinate system
     * @return Coordinates of the point in the view's coordinate system.
     */
    private fun transformCoordBitmapToTouch(
        bx: Float,
        by: Float
    ): PointF {
        matrixM!!.getValues(m)
        val origW = drawable.intrinsicWidth.toFloat()
        val origH = drawable.intrinsicHeight.toFloat()
        val px = bx / origW
        val py = by / origH
        val finalX = m!![Matrix.MTRANS_X] + imageWidth * px
        val finalY = m!![Matrix.MTRANS_Y] + imageHeight * py
        return PointF(
            finalX,
            finalY
        )
    }

    private fun compatPostOnAnimation(runnable: Runnable?) {
        postOnAnimation(runnable)
    }


    private enum class State {
        NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM
    }

    interface OnZoomImageViewListener {
        fun onMove()
    }

    /**
     * Gesture Listener detects a single click or long click and passes that on
     * to the view's listener.
     *
     * @author Ortiz
     */
    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return if (doubleTapListener != null) {
                doubleTapListener!!.onSingleTapConfirmed(e)
            } else performClick()
        }

        override fun onLongPress(e: MotionEvent) {
            performLongClick()
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (fling != null) {
                //
                // If a previous fling is still active, it should be cancelled so that two flings
                // are not run simultaenously.
                //
                fling!!.cancelFling()
            }
            fling = Fling(velocityX.toInt(), velocityY.toInt())
            compatPostOnAnimation(fling)
            return super.onFling(
                e1,
                e2,
                velocityX,
                velocityY
            )
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            var consumed = false
            if (doubleTapListener != null) {
                consumed = doubleTapListener!!.onDoubleTap(e)
            }
            if (state == State.NONE) {
                val targetZoom = if (currentZoom == minScale) maxScale else minScale
                val doubleTap = DoubleTapZoom(
                    targetZoom,
                    e.x,
                    e.y,
                    false
                )
                compatPostOnAnimation(doubleTap)
                consumed = true
            }
            return consumed
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return if (doubleTapListener != null) {
                doubleTapListener!!.onDoubleTapEvent(e)
            } else false
        }
    }

    /**
     * Responsible for all touch events. Handles the heavy lifting of drag and also sends
     * touch events to Scale Detector and Gesture Detector.
     *
     * @author Ortiz
     */
    private inner class PrivateOnTouchListener : OnTouchListener {
        //
        // Remember last point position for dragging
        //
        private val last = PointF()
        override fun onTouch(
            v: View,
            event: MotionEvent
        ): Boolean {
            mScaleDetector!!.onTouchEvent(event)
            mGestureDetector!!.onTouchEvent(event)
            val curr = PointF(
                event.x,
                event.y
            )
            if (((state == State.NONE) || state == State.DRAG) || state == State.FLING) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        last.set(curr)
                        if (fling != null) {
                            fling!!.cancelFling()
                        }
                        setState(State.DRAG)
                    }

                    MotionEvent.ACTION_MOVE -> if (state == State.DRAG) {
                        val deltaX = curr.x - last.x
                        val deltaY = curr.y - last.y
                        val fixTransX = getFixDragTrans(
                            deltaX,
                            viewWidth.toFloat(),
                            imageWidth
                        )
                        val fixTransY = getFixDragTrans(
                            deltaY,
                            viewHeight.toFloat(),
                            imageHeight
                        )
                        matrixM!!.postTranslate(
                            fixTransX,
                            fixTransY
                        )
                        fixTrans()
                        last[curr.x] = curr.y
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> setState(State.NONE)
                }
            }
            imageMatrix = matrixM

            //
            // User-defined OnTouchListener
            //
            if (userTouchListener != null) {
                userTouchListener!!.onTouch(
                    v,
                    event
                )
            }

            //
            // OnZoomImageViewListener is set: ZoomImageView dragged by user.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }

            //
            // indicate event was handled
            //
            return true
        }
    }

    /**
     * ScaleListener detects user two finger scaling and scales image.
     *
     * @author Ortiz
     */
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            setState(State.ZOOM)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleImage(
                detector.scaleFactor.toDouble(),
                detector.focusX,
                detector.focusY,
                true
            )

            //
            // OnZoomImageViewListener is set: ZoomImageView pinch zoomed by user.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            super.onScaleEnd(detector)
            setState(State.NONE)
            var animateToZoomBoundary = false
            var targetZoom: Float = currentZoom
            if (currentZoom > maxScale) {
                targetZoom = maxScale
                animateToZoomBoundary = true
            } else if (currentZoom < minScale) {
                targetZoom = minScale
                animateToZoomBoundary = true
            }
            if (animateToZoomBoundary) {
                val doubleTap: DoubleTapZoom = DoubleTapZoom(
                    targetZoom,
                    (
                            viewWidth / 2).toFloat(),
                    (
                            viewHeight / 2).toFloat(),
                    true
                )
                compatPostOnAnimation(doubleTap)
            }
        }
    }

    /**
     * DoubleTapZoom calls a series of runnables which apply
     * an animated zoom in/out graphic to the image.
     *
     * @author Ortiz
     */
    private inner class DoubleTapZoom(
        targetZoom: Float,
        focusX: Float,
        focusY: Float,
        stretchImageToSuper: Boolean
    ) :
        Runnable {
        private val startTime: Long
        private val startZoom: Float
        private val targetZoom: Float
        private val bitmapX: Float
        private val bitmapY: Float
        private val stretchImageToSuper: Boolean
        private val interpolator = AccelerateDecelerateInterpolator()
        private val startTouch: PointF
        private val endTouch: PointF

        init {
            setState(State.ANIMATE_ZOOM)
            startTime = System.currentTimeMillis()
            startZoom = currentZoom
            this.targetZoom = targetZoom
            this.stretchImageToSuper = stretchImageToSuper
            val bitmapPoint = transformCoordinatesTouchToBitmap(
                focusX,
                focusY,
                false
            )
            bitmapX = bitmapPoint.x
            bitmapY = bitmapPoint.y

            //
            // Used for translating image during scaling
            //
            startTouch = transformCoordBitmapToTouch(
                bitmapX,
                bitmapY
            )
            endTouch = PointF(
                (viewWidth / 2).toFloat(),
                (
                        viewHeight / 2).toFloat()
            )
        }

        override fun run() {
            val t = interpolate()
            val deltaScale = calculateDeltaScale(t)
            scaleImage(
                deltaScale,
                bitmapX,
                bitmapY,
                stretchImageToSuper
            )
            translateImageToCenterTouchPosition(t)
            fixScaleTrans()
            imageMatrix = matrixM

            //
            // OnZoomImageViewListener is set: double tap runnable updates listener
            // with every frame.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }
            if (t < 1f) {
                //
                // We haven't finished zooming
                //
                compatPostOnAnimation(this)
            } else {
                //
                // Finished zooming
                //
                setState(State.NONE)
            }
        }

        /**
         * Interpolate between where the image should start and end in order to translate
         * the image so that the point that is touched is what ends up centered at the end
         * of the zoom.
         *
         * @param t
         */
        private fun translateImageToCenterTouchPosition(t: Float) {
            val targetX = startTouch.x + t * (endTouch.x - startTouch.x)
            val targetY = startTouch.y + t * (endTouch.y - startTouch.y)
            val curr = transformCoordBitmapToTouch(
                bitmapX,
                bitmapY
            )
            matrixM!!.postTranslate(
                targetX - curr.x,
                targetY - curr.y
            )
        }

        /**
         * Use interpolator to get t
         *
         * @return
         */
        private fun interpolate(): Float {
            val currTime = System.currentTimeMillis()
            var elapsed = (currTime - startTime) / 500f
            elapsed = 1f.coerceAtMost(elapsed)
            return interpolator.getInterpolation(elapsed)
        }

        /**
         * Interpolate the current targeted zoom and get the delta
         * from the current zoom.
         *
         * @param t
         * @return
         */
        private fun calculateDeltaScale(t: Float): Double {
            val zoom = (startZoom + t * (targetZoom - startZoom)).toDouble()
            return zoom / currentZoom
        }


    }

    /**
     * Fling launches sequential runnables which apply
     * the fling graphic to the image. The values for the translation
     * are interpolated by the Scroller.
     *
     * @author Ortiz
     */
    private inner class Fling(
        velocityX: Int,
        velocityY: Int
    ) : Runnable {
        var scroller: CompatScroller?
        var currX: Int
        var currY: Int

        init {
            setState(State.FLING)
            scroller = CompatScroller(contextM)
            matrixM!!.getValues(m)
            val startX = m!![Matrix.MTRANS_X].toInt()
            val startY = m!![Matrix.MTRANS_Y].toInt()
            val minX: Int
            val maxX: Int
            val minY: Int
            val maxY: Int
            if (imageWidth > viewWidth) {
                minX = viewWidth - imageWidth.toInt()
                maxX = 0
            } else {
                maxX = startX
                minX = maxX
            }
            if (imageHeight > viewHeight) {
                minY = viewHeight - imageHeight.toInt()
                maxY = 0
            } else {
                maxY = startY
                minY = maxY
            }
            scroller!!.fling(
                startX,
                startY,
                velocityX,
                velocityY,
                minX,
                maxX,
                minY,
                maxY
            )
            currX = startX
            currY = startY
        }

        fun cancelFling() {
            if (scroller != null) {
                setState(State.NONE)
                scroller!!.forceFinished(true)
            }
        }

        override fun run() {

            //
            // OnZoomImageViewListener is set: ZoomImageView listener has been flung by user.
            // Listener runnable updated with each frame of fling animation.
            //
            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }
            if (scroller!!.isFinished) {
                scroller = null
                return
            }
            if (scroller!!.computeScrollOffset()) {
                val newX = scroller!!.currX
                val newY = scroller!!.currY
                val transX = newX - currX
                val transY = newY - currY
                currX = newX
                currY = newY
                matrixM!!.postTranslate(
                    transX.toFloat(),
                    transY.toFloat()
                )
                fixTrans()
                imageMatrix = matrixM
                compatPostOnAnimation(this)
            }
        }
    }

    private inner class CompatScroller(context: Context?) {
        var scroller: Scroller? = null
        var overScroller: OverScroller? = null
        var isPreGingerbread = false

        init {
            isPreGingerbread = false
            overScroller = OverScroller(context)
        }

        fun fling(
            startX: Int,
            startY: Int,
            velocityX: Int,
            velocityY: Int,
            minX: Int,
            maxX: Int,
            minY: Int,
            maxY: Int
        ) {
            if (isPreGingerbread) {
                scroller!!.fling(
                    startX,
                    startY,
                    velocityX,
                    velocityY,
                    minX,
                    maxX,
                    minY,
                    maxY
                )
            } else {
                overScroller!!.fling(
                    startX,
                    startY,
                    velocityX,
                    velocityY,
                    minX,
                    maxX,
                    minY,
                    maxY
                )
            }
        }

        fun forceFinished(finished: Boolean) {
            if (isPreGingerbread) {
                scroller!!.forceFinished(finished)
            } else {
                overScroller!!.forceFinished(finished)
            }
        }

        val isFinished: Boolean
            get() = if (isPreGingerbread) {
                scroller!!.isFinished
            } else {
                overScroller!!.isFinished
            }

        fun computeScrollOffset(): Boolean {
            return if (isPreGingerbread) {
                scroller!!.computeScrollOffset()
            } else {
                overScroller!!.computeScrollOffset()
                overScroller!!.computeScrollOffset()
            }
        }

        val currX: Int
            get() {
                return if (isPreGingerbread) {
                    scroller!!.currX
                } else {
                    overScroller!!.currX
                }
            }
        val currY: Int
            get() {
                return if (isPreGingerbread) {
                    scroller!!.currY
                } else {
                    overScroller!!.currY
                }
            }
    }

    private inner class ZoomVariables(
        var scale: Float,
        var focusX: Float,
        var focusY: Float,
        var scaleType: ScaleType
    )

    companion object {

        //
        // SuperMin and SuperMax multipliers. Determine how much the image can be
        // zoomed below or above the zoom boundaries, before animating back to the
        // min/max zoom boundary.
        //
        private const val SUPER_MIN_MULTIPLIER = .75f
        private const val SUPER_MAX_MULTIPLIER = 1.25f
    }
}