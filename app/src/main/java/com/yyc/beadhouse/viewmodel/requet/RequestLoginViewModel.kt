package com.yyc.beadhouse.viewmodel.requet

import androidx.lifecycle.MutableLiveData
import com.yyc.beadhouse.bean.BaseResponseBean
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.requestNoCheck
import me.hgj.jetpackmvvm.state.ResultState

/**
 * @Author nike
 * @Date 2023/7/6 09:28
 * @Description
 */
class RequestLoginViewModel:BaseViewModel() {


    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<BaseResponseBean<DataBean?>>>()
//
    //方式2  不用框架帮脱壳，判断结果是否成功
//    var loginResult = MutableLiveData<ResultState<ApiResponse<UserInfo>>>()


    fun login(username: String, password: String) {
        /*requestNoCheck(
            { apiService.CheckLogin("im", username, password) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )*/


    }

}