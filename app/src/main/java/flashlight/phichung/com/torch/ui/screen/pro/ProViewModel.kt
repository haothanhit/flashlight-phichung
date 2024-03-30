package flashlight.phichung.com.torch.ui.screen.pro


import com.haohao.billing.BillingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.MyApplication
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.Constant.KEY_PAYMENT_IN_APP_ADMOB
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


sealed class StatePayment {
    object Initial : StatePayment()

    object Paid : StatePayment()

    class Unpaid(val price: String) : StatePayment()
}

@HiltViewModel
class ProViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider, preferencesHelper) {


    private val _statePayment = MutableStateFlow<StatePayment>(StatePayment.Initial)
    val statePayment = _statePayment.asStateFlow()
//    fun callPayment() {
//        BillingHelper(MyApplication.instance).buyInApp(
//            MyApplication.instance,
//            KEY_PAYMENT_IN_APP_ADMOB,
//            false
//        )
//    }


    init {
        checkPaidRemoveAds()
    }

    fun checkPaidRemoveAds() {
        if (BillingHelper(MyApplication.instance).isInAppPremiumUserByInAppKey(
                KEY_PAYMENT_IN_APP_ADMOB
            )
        ) {
            // paid
           _statePayment.value = StatePayment.Paid
            getCachePreferencesHelper().stateRemoveAds = true
        } else {

            val textPrice =
                BillingHelper(MyApplication.instance).getProductPriceByKey(KEY_PAYMENT_IN_APP_ADMOB)?.price
            _statePayment.value = StatePayment.Unpaid(textPrice.toString())
            getCachePreferencesHelper().stateRemoveAds = false


        }
    }

}