package com.haohao.camposer.state

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.CameraController.VIDEO_CAPTURE

/**
 * Camera Capture mode.
 *
 * @param value internal camera capture from CameraX
 * @see IMAGE_CAPTURE
 * @see VIDEO_CAPTURE
 * */
enum class CaptureMode(internal val value: Int) {
    Image(IMAGE_CAPTURE),

    @RequiresApi(Build.VERSION_CODES.N)
    Video(VIDEO_CAPTURE),
}
