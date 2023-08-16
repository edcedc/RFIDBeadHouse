package com.yyc.beadhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.ext.ALERT_CLEAR_MUTE
import com.yyc.beadhouse.ext.ALERT_MUTE
import com.yyc.beadhouse.network.apiService
import com.yyc.beadhouse.network.stateCallback.ListDataUiState
import com.yyc.beadhouse.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * @Author nike
 * @Date 2023/7/7 12:00
 * @Description
 */
class MainModel: BaseViewModel() {

    var listBean: MutableLiveData<ListDataUiState<DataBean>> = MutableLiveData()

    //解除静音
    val muteBean: MutableLiveData<DataBean> = MutableLiveData()
    val muteAllBean: MutableLiveData<DataBean> = MutableLiveData()
    //解除警报
    val clearBean: MutableLiveData<DataBean> = MutableLiveData()
    val clearAllBean: MutableLiveData<DataBean> = MutableLiveData()

    fun onRequest() {
        request({ apiService.AlarmList(CacheUtil.getUser()!!.RoNo) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = true,
                    isEmpty = it!!.isEmpty(),
                    isFirstEmpty = true && it.isEmpty(),
                    listData = it
                )
            listBean.value = listDataUiState

        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<DataBean>()
                )
            listBean.value = listDataUiState
        })

        /*var newlistBean =ArrayList<DataBean>()
        val alertDao = AppRoomDataBase.get().getAlertDao()!!
        alertDao.findAll()!!.forEachIndexed(){index, alert ->
            var bean = DataBean()
            bean.id = "$index"
            bean.address = "$index 号门"
            bean.user = "$index "
            newlistBean.add(bean)
        }
        val listDataUiState =
            ListDataUiState(
                isSuccess = true,
                isRefresh = false,
                listData = newlistBean,
                pageSize = newlistBean.size
            )
        listBean.value = listDataUiState*/
    }

    fun onMute(type: Int, ids: String, position: Int) {
        var newType = type

        if (newType == ALERT_MUTE){
            newType = ALERT_CLEAR_MUTE
        }else{
            newType = ALERT_MUTE
        }
        request({ apiService.ClearAlarm(CacheUtil.getUser()!!.RoNo, newType, ids) }, {
            val bean = DataBean()
            bean.NoSound = newType
            bean.position = position
            muteBean.value = bean
        }, {
            LogUtils.e(it)
        })
    }

    fun onClear(type: Int, id: String, position: Int) {
        request({ apiService.ClearAlarm(CacheUtil.getUser()!!.RoNo, type, id) }, {
            val bean = DataBean()
//            bean.NoSound = type
            bean.position = position
            clearBean.value = bean
        }, {
            LogUtils.e(it)
        })
    }

    fun onAllClear(type: Int, data: MutableList<DataBean>) {
        var sb = StringBuffer()
        data.forEach {
            sb.append(it.id).append(",")
        }
        request({ apiService.ClearAlarm(CacheUtil.getUser()!!.RoNo, type, sb.toString()) }, {
            val bean = DataBean()
            clearAllBean.value = bean
        }, {
            LogUtils.e(it)
        })
    }

    fun onAllMute(type: Int, data: MutableList<DataBean>) {
        var sb = StringBuffer()
        data.forEach {
            sb.append(it.id).append(",")
        }
        request({ apiService.ClearAlarm(CacheUtil.getUser()!!.RoNo, type, sb.toString()) }, {
            val bean = DataBean()
            muteAllBean.value = bean
        }, {
            LogUtils.e(it)
        })
    }

}