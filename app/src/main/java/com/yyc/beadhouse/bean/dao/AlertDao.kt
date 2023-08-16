package com.yyc.beadhouse.bean.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yyc.beadhouse.bean.db.Alert


/**
 * @Author nike
 * @Date 2023/7/11 17:50
 * @Description
 */
@Dao
interface AlertDao {

    /**
     * 增加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg bean: Alert?)

    /**
     * 删除
     */
    @Delete
    fun delete(bean: Alert?)

    //删除数据库表全部数据
    @Query("delete from Alert")
    fun deleteAll()


    /**
     * 查询所有
     */
    @Query("SELECT * FROM Alert")
    fun findAll(): List<Alert?>?

        /**
         * 修改
         */
        @Update
        fun update(bean: Alert?)

}