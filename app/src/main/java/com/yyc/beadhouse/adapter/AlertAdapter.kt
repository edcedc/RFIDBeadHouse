package com.yyc.beadhouse.adapter

import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.graphics.Typeface
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.flyco.roundview.RoundTextView
import com.yyc.beadhouse.R
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.ext.ALERT_CLEAR
import com.yyc.beadhouse.ext.ALERT_MUTE
import com.yyc.beadhouse.ext.setAdapterAnimation
import com.yyc.beadhouse.util.SettingUtil

/**
 * @Author nike
 * @Date 2023/7/7 17:05
 * @Description
 */
class AlertAdapter (data: MutableList<DataBean>) :
    BaseQuickAdapter<DataBean, BaseViewHolder>(
        R.layout.i_alert, data) {


    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: DataBean) {
        //赋值
        item.run {
            holder.setText(R.id.tv_trigger_location, context.getString(R.string.trigger_location) + "：" + item.address)
            holder.setText(R.id.tv_alarm_user, context.getString(R.string.user) + "：" + item.user)
            holder.setText(R.id.tv_alarm_time, context.getString(R.string.time) + "：" + item.time)

            /*when(item.NoSound){
                1 ->{
                    holder.getView<RoundTextView>(R.id.tv_mute).paint.flags = STRIKE_THRU_TEXT_FLAG
                    holder.getView<RoundTextView>(R.id.tv_mute).paint.isAntiAlias = true
                }
                2 ->{
                    holder.setText(R.id.tv_elieve, context.getString(R.string.time) + "：" + item.time)
                }
            }*/

            //临时
            if (textType == 1){
                holder.getView<AppCompatTextView>(R.id.tv_trigger_location).setTypeface(null, Typeface.BOLD)
                holder.getView<AppCompatTextView>(R.id.tv_trigger_location).setTextColor(ContextCompat.getColor(context, R.color.red_ff0000))
            }else{
                holder.getView<AppCompatTextView>(R.id.tv_trigger_location).setTypeface(null, Typeface.NORMAL)
                holder.getView<AppCompatTextView>(R.id.tv_trigger_location).setTextColor(ContextCompat.getColor(context, R.color.black_121212))
            }

            if (item.NoSound == ALERT_MUTE){
                holder.getView<RoundTextView>(R.id.tv_mute).paint.flags = STRIKE_THRU_TEXT_FLAG
                holder.getView<RoundTextView>(R.id.tv_mute).paint.isAntiAlias = true
            }else{
                holder.getView<RoundTextView>(R.id.tv_mute).paint.flags =
                    holder.getView<RoundTextView>(R.id.tv_mute).paintFlags and Paint.UNDERLINE_TEXT_FLAG
            }
            holder.getView<RoundTextView>(R.id.tv_mute).setOnClickListener {
                if (item.NoSound == ALERT_MUTE)return@setOnClickListener
                onItemClickListener?.onMuteType(ALERT_MUTE, item.id, holder.adapterPosition)
            }
            holder.getView<RoundTextView>(R.id.tv_elieve).setOnClickListener {
                onItemClickListener?.onRelieveType(ALERT_CLEAR, item.id, holder.adapterPosition)
            }
            holder.itemView.setOnClickListener {

            }
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {

        fun onRelieveType(type: Int,id: String, position: Int)

        fun onMuteType(type: Int,id: String, position: Int)

        fun onItemClick(item: DataBean, position: Int)

    }

}