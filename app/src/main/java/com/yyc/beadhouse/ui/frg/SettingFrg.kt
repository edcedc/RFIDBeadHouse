package com.yyc.beadhouse.ui.frg

import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.yc.tea.api.ApiService
import com.yyc.beadhouse.R
import com.yyc.beadhouse.databinding.FSettingBinding
import com.yyc.beadhouse.ext.showMessage
import com.yyc.beadhouse.mar.MyApplication
import com.yyc.beadhouse.network.apiService
import com.yyc.beadhouse.util.CacheUtil
import com.yyc.beadhouse.viewmodel.SettingModel
import com.yyc.beadhouse.weight.PopupWindowTool
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.ext.nav

/**
 * @Author nike
 * @Date 2023/7/6 18:02
 * @Description  设置
 */
class SettingFrg:BaseFragment<SettingModel, FSettingBinding>() {

    val settingModel: SettingModel by viewModels()

    var languagePosition: Int = 0

    var languageChoosePosition: Int = 0

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        when(MyApplication.getCurrentLanguage()){
            "zh" -> {
                mViewModel.language.set(requireActivity().getString(R.string.s_chinese))
                languagePosition = 0
            }
            "zh-rHK" -> {
                mViewModel.language.set(requireActivity().getString(R.string.t_chinese))
                languagePosition = 1
            }
            "en" -> {
                mViewModel.language.set(requireActivity().getString(R.string.e_english))
                languagePosition = 2
            }
        }
        languageChoosePosition = languagePosition

        mViewModel.host.set(CacheUtil.getUrl())
    }

    inner class ProxyClick(){

        fun close(){
            nav().navigateUp()
        }

        fun setlanguage(){
            PopupWindowTool.showListDialog(activity)
                .asCenterList(getString(R.string.please_language),
                    arrayOf(
                        getString(R.string.s_chinese),
                        getString(R.string.t_chinese),
                        getString(R.string.e_english),
                    ),{ position, text ->
                        when(position){
                            0 -> mViewModel.language.set(requireActivity().getString(R.string.s_chinese))
                            1 -> mViewModel.language.set(requireActivity().getString(R.string.t_chinese))
                            2 -> mViewModel.language.set(requireActivity().getString(R.string.e_english))
                        }
                        languagePosition = position
                    }).show()
        }

        fun setSave(){
            if (languageChoosePosition != languagePosition){
                when(languagePosition){
                    0 -> {
                        MyApplication.setCurrentLanguage("zh")
                    }
                    1 ->{
                        MyApplication.setCurrentLanguage("zh-rHK")
                    }
                    2 -> {
                        MyApplication.setCurrentLanguage("en")
                    }
                }
            }
            if (mViewModel.host.get().isEmpty()){
                showToast(getString(R.string.hiht_))
                return
            }
            if (languageChoosePosition != languagePosition){
                ActivityCompat.recreate(requireActivity())
            }
            CacheUtil.setUrl(mViewModel.host.get())
            ApiService.SERVLET_URL = CacheUtil.getUrl()
            showToast(getString(R.string.release_success))
        }
    }

}