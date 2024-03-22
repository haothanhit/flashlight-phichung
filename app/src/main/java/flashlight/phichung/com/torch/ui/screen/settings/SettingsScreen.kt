package flashlight.phichung.com.torch.ui.screen.settings

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.haohao.languagepicker.allLanguage
import com.haohao.languagepicker.dataGenerator.LangFileReaderHard
import com.haohao.languagepicker.dialog.launchCountryPickerDialog
import com.haohao.languagepicker.localesLanguageApp
import com.haohao.languagepicker.setApplicationLocales
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.ui.theme.GrayColor
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor
import flashlight.phichung.com.torch.ui.theme.checkedThumbColor
import flashlight.phichung.com.torch.ui.theme.checkedTrackColor
import flashlight.phichung.com.torch.ui.theme.uncheckedThumbColor
import flashlight.phichung.com.torch.ui.theme.uncheckedTrackColor
import flashlight.phichung.com.torch.utils.feedback
import flashlight.phichung.com.torch.utils.getVersionApp
import flashlight.phichung.com.torch.utils.shareApp


object SettingsNavigation {

    const val titleScreen = "Settings"
    const val route = "settings"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = SettingsNavigation.titleScreen,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhiteColor,
                        fontSize = 14.sp

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
        },
        content = ({ padding ->
            CustomOptionUI(padding, viewModel)
        }),
    )
}

@Composable
fun CustomOptionUI(padding: PaddingValues, viewModel: SettingsViewModel) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 20.dp)
    ) {
        var stateSound by remember { mutableStateOf(viewModel.getSoundState()) }
        var stateAutomaticFlashMode by remember { mutableStateOf(viewModel.getAutomaticOnState()) }

        val activity = LocalContext.current as Activity

        Text(
            text = "TÙY CHỈNH",
            fontFamily = FontFamily.Monospace,
            color = GrayColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        CustomOptionsItem(
            icon = R.drawable.ic_language,
            mainText = "Ngôn ngữ",
            onClick = {

                activity.launchCountryPickerDialog(
                    countryFileReader = LangFileReaderHard,
                    allowSearch = false,
                    onCountryClickListener = { languageModel ->
                        val langChange = languageModel?.code ?: ""
                        Log.i("HAOHAO", "langChange :  $langChange")
                        viewModel.setLanguageApp(langChange)
                        val index = localesLanguageApp.indexOfFirst {
                            it.toLanguageTag().lowercase() == langChange.lowercase()
                        }
                        if (index != -1) {
                            Log.i("HAOHAO", "index :  $index")
                            setApplicationLocales(
                                localesLanguageApp[index].language.toString().lowercase()
                            )


                        }

                    },
                    langCodeSelect = viewModel.getLanguageApp()

                )
            },
            switchVisible = false,
            textVisible = true,
            subText = allLanguage!!.filter { it.code== viewModel.getLanguageApp()}[0].emoji
        )

        CustomOptionsItem(
            icon = R.drawable.ic_automatic_on,
            mainText = "Tự động bật",
            onClick = {},
            switchVisible = true,
            textVisible = false,
            statusSwitch = stateAutomaticFlashMode,
            onCheckedChange = {
                //  stateSound = it
                viewModel.setAutomaticOnState(it)
            }
        )

//        CustomOptionsItem(
//            icon = R.drawable.ic_shortcut,
//            mainText = "Phím tắt bật/tắt đèn pin",
//            onClick = {},
//            switchVisible = true,
//            textVisible = false,
//        )

        CustomOptionsItem(
            icon = R.drawable.ic_settings_sound,
            mainText = "Âm thanh",
            onClick = {},
            switchVisible = true,
            textVisible = false,
            statusSwitch = stateSound,
            onCheckedChange = {
                //  stateSound = it
                viewModel.setSoundState(it)
            }

        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.Gray, thickness = 1.dp
        )

        Text(
            text = "ỦNG HỘ",
            fontFamily = FontFamily.Monospace,
            color = GrayColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 16.dp)
        )

//        CustomOptionsItem(
//            icon = R.drawable.ic_more_apps,
//            mainText = "Thêm nhiều ứng dụng",
//            onClick = {},
//            switchVisible = false,
//            textVisible = false,
//            subText = ""
//        )

        CustomOptionsItem(
            icon = R.drawable.ic_feedback,
            mainText = "Phản hồi",
            onClick = { activity.feedback() },
            switchVisible = false,
            textVisible = false,
        )

        CustomOptionsItem(
            icon = R.drawable.ic_share,
            mainText = "Chia sẽ",
            onClick = { activity.shareApp() },
            switchVisible = false,
            textVisible = false,
        )

        CustomOptionsItem(
            icon = R.drawable.ic_version,
            mainText = "Phiên bản",
            onClick = {},
            switchVisible = false,
            textVisible = true,
            subText = activity.getVersionApp(),
        )

    }
}


@Composable
fun CustomOptionsItem(
    icon: Int,
    mainText: String,
    onClick: () -> Unit,
    switchVisible: Boolean,
    textVisible: Boolean,
    subText: String = "",
    statusSwitch: Boolean = false,
    onCheckedChange: (Boolean) -> Unit? = {},

    ) {
    val checked = remember { mutableStateOf(statusSwitch) }
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

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
                    tint = Color.White,
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = mainText,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (switchVisible) {
            Switch(
                modifier = Modifier.scale(0.75f),
                checked = checked.value,
                onCheckedChange = {
                    checked.value = it
                    onCheckedChange(it)
                },
                thumbContent = {
                    Icon(
                        imageVector = if (checked.value) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = "",
                        tint = if (checked.value) Color.Green else Color.Gray
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = checkedThumbColor,
                    uncheckedThumbColor = uncheckedThumbColor,
                    checkedTrackColor = checkedTrackColor,
                    uncheckedTrackColor = uncheckedTrackColor,

                    )
            )
        }
        if (textVisible) {
            Text(
                text = subText,
                fontFamily = FontFamily.Default,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    flashlight.phichung.com.torch.ui.screen.brightness.SettingsScreen()
//}