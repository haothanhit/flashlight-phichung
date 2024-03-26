package flashlight.phichung.com.torch.ui.screen.skin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.data.model.Skin
import flashlight.phichung.com.torch.ui.components.AdmobBanner
import flashlight.phichung.com.torch.ui.theme.GrayColor
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor
import kotlinx.coroutines.launch


object SkinNavigation {

    const val route = "skin"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinScreen(
    viewModel: SkinViewModel = hiltViewModel<SkinViewModel>(),
    navController: NavHostController = rememberNavController()
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.str_skin_title),
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
        },
        content = ({ innerPadding ->


            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                var skinList by remember {
                    mutableStateOf(
                        viewModel.listSkinState
                    )
                }
                var stateGrid = rememberLazyGridState()
                val coroutineScope = rememberCoroutineScope()

                LazyVerticalGrid(
                    state = stateGrid,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(skinList.size) { index ->
                        GridItem(skin = skinList[index]) {
                            // When item is clicked, toggle its selection state
                            skinList = skinList.toMutableList().also { list ->
                                val indexSelected = list.indexOf(list.find { it.selected })
                                if (indexSelected != index) {
                                    list[indexSelected] =
                                        list[indexSelected].copy(selected = !list[indexSelected].selected)
                                    list[index] = list[index].copy(selected = true)
                                    viewModel.saveSkin(skinList[index])

                                }


                            }


                        }
                    }
                }


                LaunchedEffect(true) {
                    coroutineScope.launch {
                        // Animate scroll to the first item
                        stateGrid.animateScrollToItem(index = skinList.indexOf(skinList.find { it.selected }))
                    }
                }
                Spacer(modifier = Modifier.size(15.dp))

                AdmobBanner()


            }

        }),

        )

}


@Composable
fun GridItem(skin: Skin, onClick: () -> Unit) {

    Image(
        painter = painterResource(id = skin.imageSkin),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(10.dp, RectangleShape)
            .border(
                BorderStroke(4.dp, if (skin.selected) skin.colorSkin else GrayColor),
                RoundedCornerShape(10.dp)
            )
            .clip(RectangleShape)
            .clickable {
                onClick()


            }
    )


}





