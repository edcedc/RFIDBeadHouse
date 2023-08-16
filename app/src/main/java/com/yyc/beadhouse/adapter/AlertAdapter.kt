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
import com.yyc.beadhouse.ext.ALERT_CLEAR_MUTE
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
//        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: DataBean) {
        //赋值
        item.run {
            holder.setText(R.id.tv_trigger_location, context.getString(R.string.trigger_location) + "：" + item.address)
            holder.setText(R.id.tv_alarm_user, context.getString(R.string.user) + "：" + item.user)
            holder.setText(R.id.tv_alarm_time, context.getString(R.string.time) + "：" + item.time)

            val drawableClear = ContextCompat.getDrawable(context, R.mipmap.icon_37)
            drawableClear?.setBounds(0, 0, 40, 40) // 设置图片的宽度和高度
            holder.getView<RoundTextView>(R.id.tv_elieve).setCompoundDrawables(drawableClear, null, null, null)

            when(item.NoSound){
                ALERT_MUTE ->{
                    val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_39)
                    drawable?.setBounds(0, 0, 40, 40) // 设置图片的宽度和高度
                    holder.getView<RoundTextView>(R.id.tv_mute).setCompoundDrawables(drawable, null, null, null)
                    holder.getView<RoundTextView>(R.id.tv_mute).paint.flags = STRIKE_THRU_TEXT_FLAG
                    holder.getView<RoundTextView>(R.id.tv_mute).paint.isAntiAlias = true
                }
                0, ALERT_CLEAR_MUTE ->{
                    val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_38)
                    drawable?.setBounds(0, 0, 40, 40) // 设置图片的宽度和高度
                    holder.getView<RoundTextView>(R.id.tv_mute).setCompoundDrawables(drawable, null, null, null)
                    holder.getView<RoundTextView>(R.id.tv_mute).paint.flags =
                        holder.getView<RoundTextView>(R.id.tv_mute).paintFlags and Paint.UNDERLINE_TEXT_FLAG
                }
            }

            holder.getView<RoundTextView>(R.id.tv_mute).setOnClickListener {
                onItemClickListener?.onMuteType(item.NoSound, item.id, holder.adapterPosition)
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