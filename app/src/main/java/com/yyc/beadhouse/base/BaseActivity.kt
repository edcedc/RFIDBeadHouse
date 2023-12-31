package me.hgj.jetpackmvvm.demo.app.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.ToastUtils
import com.yyc.beadhouse.ext.dismissLoadingExt
import com.yyc.beadhouse.ext.showLoadingExt
import com.yyc.beadhouse.mar.MyApplication
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import java.util.Locale

/**
 * 时间　: 2019/12/21
 * 作者　: hegaojian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }


    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return if (event.x > left && event.x < right
                && event.y > top && event.y < bottom
            ) {
                // 点击的是输入框区域，保留点击EditText的事件
                false
            } else {

                true
            }
        }
        return false
    }

    /* *//**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     *//*
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }*/

    override fun attachBaseContext(newBase: Context) {
        val newLocale = MyApplication.getCurrentLanguage() // 获取当前选择的语言
        val context = LanguageContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

    class LanguageContextWrapper(base: Context) : ContextWrapper(base) {

        companion object {
            @SuppressLint("ObsoleteSdkInt")
            fun wrap(context: Context, language: String): ContextWrapper {
                val config = Configuration(context.resources.configuration)
                val locale = when (language) {
                    "en" -> Locale.ENGLISH
                    "zh" -> Locale.SIMPLIFIED_CHINESE
                    "zh-rHK" -> Locale.TRADITIONAL_CHINESE
                    else -> Locale.getDefault()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.setLocale(locale)
                } else {
                    config.locale = locale
                }

                val newContext = context.createConfigurationContext(config)
                return LanguageContextWrapper(newContext)
            }
        }
    }


    fun showToast(text: String?){
        ToastUtils.showShort(text)
    }

}