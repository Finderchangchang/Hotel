package liuliu.hotel.method.inter

import liuliu.hotel.method.key
import liuliu.hotel.method.utils
import liuliu.hotel.web.SoapObjectUtils
import liuliu.hotel.web.WebServiceUtils
import java.util.*

/**
 * 内宾管理
 * Created by Administrator on 2017/4/18.
 */
interface INB {
    fun downHotel(url: Boolean)
}

class LNB(var down: IDown) {
    /**
     * 下载旅馆信息
     * */
    fun downHotel() {
        var method = "GetLGInfoByLGDM"
        val map = HashMap<String, String>()
        map.put("lgdm", utils().getCache(key.hotel_id))
        WebServiceUtils.getMethod(method) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, method)
            if (model.isSuccess) {
                //down.downHotel(true)
            }
        }
    }

    /**
     * 下载房号
     * */
    fun downRooms() {
        var method = "GetFH"
        val map = HashMap<String, String>()
        map.put("lgdm", utils().getCache(key.hotel_id))
        WebServiceUtils.getMethod(method) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, method)
            if (model.isSuccess) {

            }
        }
    }

    /**
     * 下载从业人员
     * */
    fun downEmployee(hotelId: String) {
        var method = "DownHotelEmployee"
        val map = HashMap<String, String>()
        map.put("lgdm", hotelId)
        WebServiceUtils.getMethod(method) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, method)
            if (model.isSuccess) {

            }
        }
    }

}
