package flashlight.phichung.com.torch.ui.screen.home


import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {


}