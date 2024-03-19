package flashlight.phichung.com.torch.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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