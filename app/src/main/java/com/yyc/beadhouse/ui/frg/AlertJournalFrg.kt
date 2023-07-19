package com.yyc.beadhouse.ui.frg

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.yyc.beadhouse.adapter.AlertJournalAdapter
import com.yyc.beadhouse.ext.init
import com.yyc.beadhouse.ext.initClose
import com.yyc.beadhouse.ext.loadServiceInit
import com.yyc.beadhouse.ext.showLoading
import com.yyc.beadhouse.viewmodel.AlertJournalModel
import com.yyc.beadhouse.weight.recyclerview.SpaceItemDecoration
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yc.reid.api.UIHelper
import com.yyc.beadhouse.R
import com.yyc.beadhouse.databinding.BTitleRecyclerBinding
import com.yyc.beadhouse.ext.initFooter
import com.yyc.beadhouse.ext.loadListData
import com.yyc.beadhouse.ext.showEmpty
import com.yyc.beadhouse.ext.showError
import com.yyc.beadhouse.util.DatePickerUtils
import com.yyc.beadhouse.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.ext.nav
import java.text.SimpleDateFormat

/**
 * @Author nike
 * @Date 2023/7/7 16:51
 * @Description  警报日记
 */
class AlertJournalFrg: BaseFragment<AlertJournalModel, BTitleRecyclerBinding>() {

    //界面状态管理者
    lateinit var loadsir: LoadService<Any>

    val adapter: AlertJournalAdapter by lazy { AlertJournalAdapter(arrayListOf()) }

    private val alertJournalModel: AlertJournalModel by viewModels()

    var time: String = ""

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.includeToolbar.toolbar.run {
            initClose(getString(R.string.alert_journal)) {
                nav().navigateUp()
            }
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_set -> {
                        DatePickerUtils.getYearMonthDayPicker(activity, getString(R.string.filter_date)) { year, month, day ->
                            time = year + "-" + month + "-" + day
                            mDatabind.swipeRefresh.isRefreshing = true
                            mViewModel.onRequest(time, true)
                        }
                    }
                }
                true
            }
        }

        //初始化recyclerView
        mDatabind.recyclerView.init(LinearLayoutManager(context), adapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                mViewModel.onRequest(time, false)
            })
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            mViewModel.onRequest(time, true)
        }

        //初始化 SwipeRefreshLayout  刷新
        mDatabind.swipeRefresh.init {
            mViewModel.onRequest(time, true)
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.listData.observe(viewLifecycleOwner, Observer {
          if (it.isSuccess) {
                loadListData(it, adapter, loadsir, mDatabind.recyclerView, mDatabind.swipeRefresh, it.pageSize)
            } else {
                //失败
                loadsir.showError(it.errMessage)
            }
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        time = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd"))
        mViewModel.onRequest(time, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.setSupportActionBar(null)
    }

}