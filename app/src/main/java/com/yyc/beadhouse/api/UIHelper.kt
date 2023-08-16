package com.yc.reid.api

import android.os.Bundle
import androidx.navigation.NavController
import com.blankj.utilcode.util.ActivityUtils
import com.yyc.beadhouse.MainActivity
import com.yyc.beadhouse.R
import com.yyc.beadhouse.ui.act.LoginAct
import me.hgj.jetpackmvvm.ext.navigateAction


/**
 * Created by Administrator on 2017/2/22.
 */

class UIHelper private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        fun startMainAct() {
            ActivityUtils.startActivity(MainActivity::class.java)
        }

        /**
         *  登录
         */
        fun startLoginAct() {
            ActivityUtils.startActivity(LoginAct::class.java)
        }

        /**
         *  设置
         */
        fun startSettingFrg(nav: NavController) {
            val bundle = Bundle()
            bundle.putString("key", "test")
            nav.navigateAction(R.id.action_loginfragment_to_settingFrg, bundle)
        }

        /**
         *  警报日记
         */
        fun startAlertJournalFrg(nav: NavController) {
            val bundle = Bundle()
            nav.navigateAction(R.id.action_mainFrg_to_alertJournalFrg, bundle)
        }
    }
}

