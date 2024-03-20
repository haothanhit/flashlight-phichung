package flashlight.phichung.com.torch.ui.screen.gallery

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.camera.data.local.datasource.FileDataSource
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,
    private val fileDataSource: FileDataSource,
) : BaseViewModel(contextProvider) {

    private val _uiState = MutableStateFlow(
        fileDataSource.externalFiles.orEmpty().run {
            if (isEmpty()) GalleryUiState.Empty else GalleryUiState.Success(this)
        }
    ).stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = GalleryUiState.Initial
    )
    val uiState: StateFlow<GalleryUiState> get() = _uiState
}

sealed interface GalleryUiState {
    object Initial : GalleryUiState
    object Empty : GalleryUiState
    data class Success(val images: List<File>) : GalleryUiState
}