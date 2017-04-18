package liuliu.hotel.ui.activity

import android.content.Intent
import android.os.Bundle
import gd.hotel.base.BActivity
import kotlinx.android.synthetic.main.activity_passenger_manager.*

import liuliu.hotel.R

/**
 * 内宾管理
 * */
class PassengerManagerActivity : BActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_passenger_manager
    }

    override fun initViews() {
        toolbar.setLeftClick { finish() }
        //内宾添加
        person_add_btn.setOnClickListener { startActivity(Intent(this, RegPersonActivity::class.java)) }
    }

    override fun initEvents() {

    }
}
