package com.yyc.beadhouse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.LogUtils
import com.yyc.beadhouse.databinding.AMainBinding
import com.yyc.beadhouse.viewmodel.NotificationModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity<NotificationModel, AMainBinding>() {

    var exitTime = 0L
    override fun initView(savedInstanceState: Bundle?) {
//        appViewModel.appColor.value?.let {
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
//            StatusBarUtil.setColor(this, it, 0)
//        }

        /*onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFrg) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })*/
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent!!.extras
        if (extras != null) {
            mViewModel.notification.value = extras.getString("id")
        }
    }

}