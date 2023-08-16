package com.yyc.beadhouse.ui.frg

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.LogUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.yc.reid.api.UIHelper
import com.yyc.beadhouse.R
import com.yyc.beadhouse.bean.AppRoomDataBase
import com.yyc.beadhouse.bean.db.Alert
import com.yyc.beadhouse.databinding.FLoginBinding
import com.yyc.beadhouse.ext.showMessage
import com.yyc.beadhouse.viewmodel.LoginModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.ext.nav
import org.json.JSONArray
import org.json.JSONObject


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
           /* val alertDao = AppRoomDataBase.get().getAlertDao()!!
            for (i in 0..19) {
                val bean = Alert()
                var obj = JSONObject()
                obj.put("text", "$i 号门")
                obj.put("xxx", "$i 000号门")
                var json = JSONArray()
                json.put(obj)
                bean.alertAddress = json.toString()
                alertDao.add(bean)
            }

            alertDao.findAll()!!.forEachIndexed(){index, alert ->
                LogUtils.e(index, alert)
            }*/

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
