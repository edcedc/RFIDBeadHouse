package com.yyc.beadhouse.bean


import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by yc on 2017/8/17.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class DataBean(
    var id: String="",
    var LoginID: String="",
    var RoNo: String="",
    var address: String="",
    var showUser: String="",
    var showType: Int = 0,
    var showTime: String="",
    var time: String="",
    var user: String="",
    var token: String="",
    var relieveType: Int = 0,
    var muteType: Int = 0,
    var position: Int = 0,
    var type: Int = 0,
    var NoSound: Int = 0, //是否静音：1是0否
    var textType: Int = 0
) : Parcelable