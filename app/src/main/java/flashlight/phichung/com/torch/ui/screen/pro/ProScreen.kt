package flashlight.phichung.com.torch.ui.screen.pro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.ui.theme.BottomCardShape
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.Skin9Color
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor


object ProNavigation {

    const val titleScreen = "Pro"
    const val route = "pro"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProScreen(
    viewModel: ProViewModel = hiltViewModel<ProViewModel>(),
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = ProNavigation.titleScreen,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhiteColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.KeyboardArrowLeft, null, tint = IconWhiteColor)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )


            )
//            TopAppBar(
//                title = {
//                    Text(
//                        text = ProNavigation.titleScreen,
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.headlineMedium,
//                        color = TextWhiteColor
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(Icons.Filled.KeyboardArrowLeft, null, tint = IconWhiteColor)
//                    }
//                }, colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            )
        },
        content = ({ padding ->
            CustomProUI(padding, viewModel)
        }),
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomProUI(padding: PaddingValues, viewModel: ProViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_flashligh_pro),
                contentDescription = "ProScreen",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Transparent
            )
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2.5f),
                colors = CardDefaults.cardColors(Color.Green),
                elevation = CardDefaults.cardElevation(),
                shape = BottomCardShape.large
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .weight(4f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PRO",
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Skin9Color,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        CustomProOptionUI(
                            icon = R.drawable.ic_pro,
                            mainText = "Loại bỏ các quảng cáo."
                        )
                        CustomProOptionUI(
                            icon = R.drawable.ic_pro,
                            mainText = "Phím tắt bật/tắt đèn pin."
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .wrapContentSize(),
                    ) {
                        RoundedButton(
                            text = "234.000đ",
                            onClick = {}
                        )
                    }

                }

            }
        }
    }

}

@Composable
fun RoundedButton(text: String, onClick: () -> Unit) {
    val description = AnnotatedString.Builder().apply {
        pushStyle(SpanStyle(color = Color.Black))
        append("Ưu đãi có giới hạn dành cho bạn: ")

        pushStyle(SpanStyle(color = Color.Red))
        append("50% GIẢM GIÁ")
        pop()
    }.toAnnotatedString()
    Text(
        modifier = Modifier
            .padding(10.dp),
        text = description,
        style = MaterialTheme.typography.headlineMedium,
        color = Skin9Color,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Skin9Color),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun CustomProOptionUI(icon: Int, mainText: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.Transparent),
                Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    tint = Color.Black,
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = mainText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

