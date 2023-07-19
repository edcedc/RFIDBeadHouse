package com.yyc.beadhouse.ui.frg

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.LogUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.yc.reid.api.UIHelper
import com.yyc.beadhouse.R
import com.yyc.beadhouse.databinding.FLoginBinding
import com.yyc.beadhouse.ext.showMessage
import com.yyc.beadhouse.viewmodel.LoginModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.ext.nav


/**
 * @Author nike
 * @Date 2023/7/5 14:53
 * @Description
 */
class LoginFrg : BaseFragment<LoginModel, FLoginBinding>() {

    val loginModel: LoginModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(loginModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
    }

    inner class ProxyClick() {

        fun clear() {
            mViewModel.username.set("")
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }

        fun login() {
            if (!isGooglePlayServicesAvailable()) {
                return
            }
            when {
                mViewModel.username.get().isEmpty() -> showMessage(getString(R.string.error_phone))
                mViewModel.password.get().isEmpty() -> showMessage(getString(R.string.error_phone))
                else ->
                    loginModel.login(
                        mViewModel.username.get(),
                        mViewModel.password.get()
                    )
            }
        }

        fun toSet() {
            UIHelper.startSettingFrg(nav())
        }
    }

    //判断手机是否安装google设备
    fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404)?.show()
            }
            return false
        }
        return true
    }

}
