package liuliu.hotel.method

import liuliu.hotel.model.RoomModel
import java.util.*

/**
 * Created by Administrator on 2017/4/18.
 */
class room {
    internal var list: MutableList<RoomModel> = ArrayList<RoomModel>()
    /**
     * 房号添加
     * */
    fun add(): Boolean {
        list.add(RoomModel("Addroom", 0))
        return true
    }

    /**
     * 根据roomid删除房间信息
     * */
    fun delete(roomId: String): Boolean {
        return true
    }

    /**
     * 查询出所有的房号
     * */
    fun getAll(): MutableList<RoomModel> {
        for (i in 0..4) {
            list.add(RoomModel("room:" + i, i))
        }
        return list
    }
}