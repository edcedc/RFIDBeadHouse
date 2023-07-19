package com.yyc.beadhouse.bean.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.rxjava3.annotations.NonNull
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

/**
 * @Author nike
 * @Date 2023/7/11 17:31
 * @Description
 */
@Entity
open class Alert {

    @PrimaryKey(autoGenerate = true)//自增长
    var uid: Int = 0

    @ColumnInfo(name = "alert_user")
    var alertUser : String? = null
    @ColumnInfo(name = "alert_time")
    var alertTime : String? = null
    @ColumnInfo(name = "alert_address")
    var alertAddress : String? = null

}