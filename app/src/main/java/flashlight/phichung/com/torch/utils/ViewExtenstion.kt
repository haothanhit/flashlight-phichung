package flashlight.phichung.com.torch.utils

import androidx.compose.ui.graphics.Color

fun hexToColor(hex: String): Color {
    // Remove '#' symbol if present
    val hexWithoutHash = if (hex.startsWith("#")) hex.substring(1) else hex

    // Convert hexadecimal to integer value
    val colorInt = hexWithoutHash.toInt(16)

    // Add full opacity (0xFF) to the color
    val colorWithAlpha = 0xFF000000.toInt() or colorInt

    // Create Color object from integer value
    return Color(colorWithAlpha)
}