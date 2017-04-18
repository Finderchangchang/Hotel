package liuliu.hotel.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gd.hotel.base.BActivity
import kotlinx.android.synthetic.main.activity_room_manage.*

import liuliu.hotel.R
import liuliu.hotel.method.room
import liuliu.hotel.model.DBLGInfo
import liuliu.hotel.model.RoomModel
import net.tsz.afinal.utils.CommonAdapter
import net.tsz.afinal.utils.ViewHolder
import java.util.*

/**
 * 房号管理
 * */
class RoomManageActivity : BActivity() {
    internal var list: MutableList<RoomModel> = ArrayList<RoomModel>()
    internal var adapter: CommonAdapter<RoomModel>? = null
    override fun setLayout(): Int {
        return R.layout.activity_room_manage
    }

    override fun initViews() {
        list = room().getAll()
        toolbar.setLeftClick { finish() }
        toolbar.setRightClick { startActivityForResult(Intent(this, AddRoomActivity::class.java), 10) }
        room_srl.setOnRefreshListener {
            refresh()
        }
    }

    override fun initEvents() {
        adapter = object : CommonAdapter<RoomModel>(this, list, R.layout.header_layout) {
            override fun convert(holder: ViewHolder, dblgInfo: RoomModel, position: Int) {
                holder.setText(R.id.tip, dblgInfo.roomId)
            }
        }
        main_lv!!.adapter = adapter
        refresh()
    }

    fun refresh() {
        list = room().getAll()
        adapter!!.notifyDataSetChanged()
        room_srl.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 9) {

        }
    }
}
