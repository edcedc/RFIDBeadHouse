package com.yyc.beadhouse.bean

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yyc.beadhouse.bean.dao.AlertDao
import com.yyc.beadhouse.bean.db.Alert
import com.yyc.beadhouse.mar.MyApplication


/**
 * @Author nike
 * @Date 2023/7/11 18:00
 * @Description
 */
@Database(entities = [Alert::class
                     ], version = 1, exportSchema = false)
abstract class AppRoomDataBase : RoomDatabase() {

    abstract fun getAlertDao(): AlertDao?

    companion object {

        const val DATABASE_NAME = "beadhouse_app.db"

        @Volatile
        private var databaseInstance: AppRoomDataBase? = null

        @Synchronized
        @JvmStatic
        fun get(): AppRoomDataBase {
            if (databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(
                    MyApplication.get(),
                    AppRoomDataBase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()//允许在主线程操作数据库，一般不推荐；设置这个后主线程调用增删改查不会报错，否则会报错
//                    .openHelperFactory(factory)
                    .build()
            }
            return databaseInstance!!
        }

    }

}