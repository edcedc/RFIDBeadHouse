package com.yyc.beadhouse.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyc.beadhouse.bean.BaseListBean
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.network.apiService
import com.yyc.beadhouse.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.ext.requestNoCheck

/**
 * @Author nike
 * @Date 2023/7/7 16:51
 * @Description
 */
class AlertJournalModel: BaseViewModel() {

    var listData: MutableLiveData<ListDataUiState<DataBean>> = MutableLiveData()

    private var pagerNumber = 1

    fun onRequest(time: String, isRefresh: Boolean) {
        if (isRefresh) {
            pagerNumber = 1
        }
        requestNoCheck({apiService.GetAlertsLog(time, pagerNumber)},{
            pagerNumber ++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isEmpty = it.data!!.isEmpty(),
                    isRefresh = isRefresh,
                    isFirstEmpty = isRefresh && it.data!!.isEmpty(),
                    listData = it.data,
                    pageSize = it.count
                )
            listData.value = listDataUiState

        },{
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<DataBean>()
                )
            listData.value = listDataUiState
        })
        /*
        request({ apiService.GetAlertsLog(dasta, pageNo) }, {
//            val isRefresh = true
//            pagerNumber += 1
            pageNo ++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isEmpty = it!!.isEmpty(),
                    hasMore = false,
                    isRefresh = isRefresh,
                    isFirstEmpty = pageNo == 1 && it.isEmpty(),
                    listData = it
                )
            listData.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = true,
                    listData = arrayListOf<DataBean>()
                )
            listData.value = listDataUiState
            LogUtils.e("分頁沒數據")
        })*/
    }

}