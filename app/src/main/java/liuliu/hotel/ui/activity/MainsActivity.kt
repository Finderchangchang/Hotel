package liuliu.hotel.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gd.hotel.base.BActivity
import kotlinx.android.synthetic.main.activity_mains.*

import liuliu.hotel.R

/**
 * 首页
 * */
class MainsActivity : BActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_mains
    }

    override fun initViews() {
        //内宾登记
        nb_tv.setOnClickListener { startActivity(Intent(this, PassengerManagerActivity::class.java)) }
        //团队管理
        td_tv.setOnClickListener { startActivity(Intent(this, MainsActivity::class.java)) }
        //房号管理
        fh_tv.setOnClickListener { startActivity(Intent(this, RoomManageActivity::class.java)) }
        //从业人员管理
        cy_tv.setOnClickListener { startActivity(Intent(this, MainsActivity::class.java)) }
        //个人中心
        gr_tv.setOnClickListener { startActivity(Intent(this, MainsActivity::class.java)) }
        //通知通告
        tz_tv.setOnClickListener { startActivity(Intent(this, MainsActivity::class.java)) }
    }

    override fun initEvents() {

    }
}
