package liuliu.hotel.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gd.hotel.base.BActivity

import net.tsz.afinal.utils.CommonAdapter
import net.tsz.afinal.utils.ViewHolder

import liuliu.hotel.R
import liuliu.hotel.model.DBLGInfo

/**
 * 添加房号（房号，楼层，床位数）
 * */
class AddRoomActivity : BActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_add_room
    }

    override fun initViews() {

    }

    override fun initEvents() {
    }
}
