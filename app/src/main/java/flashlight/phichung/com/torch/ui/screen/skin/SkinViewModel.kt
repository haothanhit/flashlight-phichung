package flashlight.phichung.com.torch.ui.screen.skin


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.data.model.Skin
import flashlight.phichung.com.torch.ui.theme.Skin5Color
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import flashlight.phichung.com.torch.utils.listSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SkinViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {



    var listSkinState by mutableStateOf(listOf<Skin>())




    init {

        listSkinState = getListSkin()
    }


     private fun getListSkin(): List<Skin> {

        val listSkinSelect = listSkin.map { it.copy() }
        val skinLocal = preferencesHelper.skin

        if (skinLocal != null) {
            listSkinSelect.find { it.idSkin == skinLocal.idSkin }?.selected = true
        } else {
            listSkinSelect[0].selected = true
        }

        return listSkinSelect
    }


    fun saveSkin(skin: Skin) {
        preferencesHelper.skin = skin

    }
}