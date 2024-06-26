package com.haohao.camposer.state

import androidx.camera.view.PreviewView.ScaleType as CameraScaleType

/**
 * Camera scale type.
 *
 * @param type internal scale type from cameraX
 * @see CameraScaleType
 * */
enum class ScaleType(val type: CameraScaleType) {
    FitStart(CameraScaleType.FIT_START),
    FitCenter(CameraScaleType.FIT_CENTER),
    FitEnd(CameraScaleType.FIT_END),
    FillStart(CameraScaleType.FILL_START),
    FillCenter(CameraScaleType.FILL_CENTER),
    FillEnd(CameraScaleType.FILL_END),
}
