package flashlight.phichung.com.torch.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.Color
import flashlight.phichung.com.torch.R

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


fun Activity.getVersionApp(): String {
    try {
        val manager: PackageManager = this
            .packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(
                this
                    .packageName,
                0
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        assert(info != null)
        return info!!.versionName.toString()

    } catch (ex: Exception) {
    }
    return ""
}

 fun Activity.shareApp() {
    val sendIntent = Intent(Intent.ACTION_SEND)
    sendIntent.type = "text/plain"
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        """ Hey, look this app ! 
 https://play.google.com/store/apps/details?id=${this.packageName}"""
    )
    val shareIntent =
        Intent.createChooser(sendIntent, "Share this app with your friends !")
    startActivity(shareIntent)
}


 fun Activity.feedback() {


    val deviceInfo = buildString {
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\n")
        append("\nSystem Info:")
        append("\nAndroid Device: " + Build.MODEL + " (" + Build.MANUFACTURER + ")")
        append("\nAndroid version: " + Build.VERSION.RELEASE)
        append("\n Device: " + Build.DEVICE)
        append("\n OS API Level: " + Build.VERSION.SDK_INT)
        append("\n App version: " + getVersionApp())

    }

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:") // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_support)))
    intent.putExtra(
        Intent.EXTRA_SUBJECT,
        getString(R.string.email_subject_format, getString(R.string.app_name))
    )
    intent.putExtra(Intent.EXTRA_TEXT, deviceInfo)
    startActivity(Intent.createChooser(intent, "Send email..."))


}