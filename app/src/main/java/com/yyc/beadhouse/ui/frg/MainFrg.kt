package com.yyc.beadhouse.ui.frg

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.kingja.loadsir.core.LoadService
import com.yc.reid.api.UIHelper
import com.yyc.beadhouse.R
import com.yyc.beadhouse.adapter.AlertAdapter
import com.yyc.beadhouse.bean.DataBean
import com.yyc.beadhouse.databinding.FMainBinding
import com.yyc.beadhouse.event.NotificationEvent
import com.yyc.beadhouse.ext.ALERT_CLEAR
import com.yyc.beadhouse.ext.ALERT_MUTE
import com.yyc.beadhouse.ext.init
import com.yyc.beadhouse.ext.loadListData
import com.yyc.beadhouse.ext.loadServiceInit
import com.yyc.beadhouse.ext.moveToPosition
import com.yyc.beadhouse.ext.showEmpty
import com.yyc.beadhouse.ext.showLoading
import com.yyc.beadhouse.viewmodel.MainModel
import com.yyc.beadhouse.viewmodel.NotificationModel
import com.yyc.beadhouse.weight.PopupWindowTool
import com.yyc.beadhouse.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.ext.nav
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @Author nike
 * @Date 2023/7/7 11:59
 * @Description
 */
class MainFrg : BaseFragment<MainModel, FMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {

    //界面状态管理者
    lateinit var loadsir: LoadService<Any>

    val adapter: AlertAdapter by lazy { AlertAdapter(arrayListOf()) }

    private val notificationModel: NotificationModel by activityViewModels()

    var exitTime = 0L

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.navView.setNavigationItemSelectedListener(this)
        mDatabind.click = ProxyClick()
        setHasOptionsMenu(true)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        mDatabind.includeToolbar.toolbar.run {
            init(getString(R.string.app_name))
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_set -> {
                        onOpenDrawer()
                    }
                }
                true
            }
        }

        //初始化recyclerView
        mDatabind.recyclerView.init(LinearLayoutManager(context), adapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f), false))
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            mViewModel.onRequest()
        }

        //初始化 SwipeRefreshLayout  刷新
        mDatabind.swipeRefresh.init {
            mViewModel.onRequest()
        }

        adapter.run{
            setOnItemClickListener(object : AlertAdapter.OnItemClickListener{
                override fun onRelieveType(type: Int, id: String, position: Int) {
                    mViewModel.onClear(type, id, position)
                }

                override fun onMuteType(type: Int, id: String, position: Int) {
                    mViewModel.onMute(type, id, position)
                }

                override fun onItemClick(item: DataBean, position: Int) {
                    TODO("Not yet implemented")
                }

            })
        }

        /*requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mDatabind.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                        mDatabind.drawerLayout.closeDrawer(GravityCompat.END)
                    } else {
                        //是主页
                        if (System.currentTimeMillis() - exitTime > 2000) {
                            ToastUtils.showShort("再按一次退出程序")
                            exitTime = System.currentTimeMillis()
                        } else {
                            activity!!.finish()
                        }
                    }
                }
            })*/

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                EventBus.getDefault().unregister(this)
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStockTakeEvent(event: NotificationEvent){
        setRecyMove(event.msg)
    }

    private fun setRecyMove(msg: String) {
        if (!msg.isEmpty()) {
            adapter.data.forEachIndexed() { index, dataBean ->
                if (dataBean.id.equals(msg)) {
                    moveToPosition(mDatabind.recyclerView, index)
                    dataBean.textType = 1
                    adapter.notifyItemChanged(index)
                    return
                }
            }
        }
    }

    //region数据回调
    override fun createObserver() {
        super.createObserver()
         mViewModel.listBean.observe(viewLifecycleOwner, Observer {
             loadListData(it, adapter, loadsir, mDatabind.recyclerView, mDatabind.swipeRefresh, it.pageSize)
             if (!mDatabind.layout.isVisible)mDatabind.layout.visibility = View.VISIBLE
         })

        mViewModel.muteBean.observe(viewLifecycleOwner,{
            when(it.position){
                -1 ->{
                    adapter.data.forEach {
                        it.NoSound = 1
                    }
                    adapter.notifyDataSetChanged()
                }
                -2 ->{
                    adapter.data.clear()
                    adapter.notifyDataSetChanged()
                    loadsir.showEmpty()
                }
                else ->{
                    if (it.NoSound == 1){
                        val bean = adapter.data[it.position]
                        bean.NoSound = it.NoSound
                        adapter.setData(it.position, bean)
                    }else{
                        adapter.removeAt(it.position)
                    }
                }
            }
        })

        notificationModel.notification.observe(viewLifecycleOwner, Observer {
            setRecyMove(it)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        mViewModel.onRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.setSupportActionBar(null)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDatabind.drawerLayout.postDelayed({
            when (item.itemId) {
                R.id.item_alert -> {
                    UIHelper.startAlertJournalFrg(nav())
                }
                R.id.nav_login ->{
                    showLoading("DeleteToken...")
                    FirebaseMessaging.getInstance().deleteToken()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                dismissLoading()
                                UIHelper.startLoginAct()
                                requireActivity().finish()
                            }
                        }

                }
            }
            mDatabind.drawerLayout.closeDrawer(GravityCompat.END)
        }, 300)
        return true
    }

    //region  抽屉布局
    fun onOpenDrawer() {
        if (!mDatabind.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDatabind.drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    //region 点击事件
    inner class ProxyClick(){

        fun onAllClear(){
            PopupWindowTool.showDialog(activity).asConfirm(
                getText(R.string.app_name), getText(R.string.hint_2),
                getText(R.string.cancel), getText(R.string.confirm),{
                    var sb = StringBuffer()
                    adapter.data.forEach {
                        sb.append(it.id).append(",")
                    }
                    mViewModel.onClear(ALERT_CLEAR, sb.toString(), -2)
                }, {

                } , false
            ).show()
        }

        fun onAllMute(){
            PopupWindowTool.showDialog(activity).asConfirm(
                getText(R.string.app_name), getText(R.string.hint_1),
                getText(R.string.cancel), getText(R.string.confirm),{
                    var sb = StringBuffer()
                    adapter.data.forEach {
                        sb.append(it.id).append(",")
                    }
                    mViewModel.onMute(ALERT_MUTE, sb.toString(), -1)
                }, {

                } , false
            ).show()

        }
    }

}