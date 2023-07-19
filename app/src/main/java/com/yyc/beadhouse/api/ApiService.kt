package com.yc.tea.api

import com.yyc.beadhouse.bean.BaseListBean
import com.yyc.beadhouse.bean.BaseResponseBean
import com.yyc.beadhouse.bean.DataBean
import retrofit2.http.*


/**
 * Created by xuhao on 2017/11/16.
 * Api 接口
 */

interface ApiService{

    companion object {

        private val url =
//            "192.168.2.18"
            "47.243.120.137"

        var SERVLET_URL = "http://" +
                url + "/RFIDBeadHouseWebService/MobileWebService.asmx/"
    }

    //登录
    @FormUrlEncoded
    @POST("CheckLogin")
    suspend fun CheckLogin(
        @Field("token") token: String,
        @Field("loginID") loginID: String,
        @Field("userPwd") userPwd: String
    ): BaseResponseBean<DataBean>

    //警报列表
    @FormUrlEncoded
    @POST("AlarmList")
    suspend fun AlarmList(
        @Field("userid") userid: String
    ): BaseListBean<ArrayList<DataBean>>

    //警报日记
    @FormUrlEncoded
    @POST("GetAlertsLog")
    suspend fun GetAlertsLog(
        @Field("alarmDate") alarmDate: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int = 20
    ): BaseListBean<ArrayList<DataBean>>

    //取消警报/静音
    @FormUrlEncoded
    @POST("ClearAlarm")
    suspend fun ClearAlarm(
        @Field("userid") userid: String,
        @Field("type") type: Int,
        @Field("alertID") alertID: String,
        @Field("alertType") alertType: Int = 2,
    ): BaseResponseBean<DataBean?>


}