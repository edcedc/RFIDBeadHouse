package com.yyc.beadhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yyc.beadhouse.bean.DataBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @Author nike
 * @Date 2023/7/17 11:03
 * @Description
 */
class NotificationModel: BaseViewModel() {

    val notification: MutableLiveData<String> = MutableLiveData()

}