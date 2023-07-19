package com.yyc.beadhouse.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyc.beadhouse.R
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.ext.setAdapterAnimation
import com.yyc.beadhouse.util.SettingUtil

/**
 * @Author nike
 * @Date 2023/7/7 17:05
 * @Description
 */
class AlertJournalAdapter (data: ArrayList<DataBean>) :
    BaseQuickAdapter<DataBean, BaseViewHolder>(
        R.layout.i_alert_journal, data) {


    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: DataBean) {
        //赋值
        item.run {
            holder.setText(R.id.tv_trigger_location, context.getString(R.string.trigger_location) + "：" + item.address)
            holder.setText(R.id.tv_alarm_user, context.getString(R.string.user) + "：" + item.user)
            holder.setText(R.id.tv_alarm_time, context.getString(R.string.time) + "：" + item.time)

            holder.setText(R.id.tv_release_time, context.getString(R.string.release_user) + "：" + item.showUser)
            holder.setText(R.id.tv_release_type, context.getString(R.string.release_type) + "："
                    + if (item.showType == 1) context.getString(R.string.release_type1) else context.getString(R.string.release_type2))
            holder.setText(R.id.tv_release_user, context.getString(R.string.release_time) + "：" + item.showTime)

            holder.itemView.setOnClickListener {

            }
        }
    }

}