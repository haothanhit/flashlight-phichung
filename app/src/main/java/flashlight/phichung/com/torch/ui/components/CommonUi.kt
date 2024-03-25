package flashlight.phichung.com.torch.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.ui.theme.GrayColor
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor


/**
 * Button child with border and icon
 *
 * @param modifier
 * @param icon
 * @param clickAction
 * @receiver
 */
@Composable
fun ButtonChild(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    clickAction: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable { clickAction.invoke() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
//            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(IconWhiteColor),
            modifier = Modifier
                .size(50.dp)
                .border(
                    BorderStroke(2.dp, GrayColor),
                    CircleShape
                )
                .padding(10.dp)
                .clip(CircleShape)
        )
    }


}

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
        )
    }
}

@Composable
fun AdmobBanner(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(AdSize.BANNER.height.dp)) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->


                // on below line specifying ad view.
                AdView(context).apply {
                    // on below line specifying ad size
                    //adSize = AdSize.BANNER
                    // on below line specifying ad unit id
                    // currently added a test ad unit id.
                    setAdSize(AdSize.BANNER)
                    adUnitId = context.getString(R.string.str_ads_banner)
                    // calling load ad to load our ad.
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }

}