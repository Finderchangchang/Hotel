package liuliu.hotel.method.inter

import liuliu.hotel.model.DBTZTGInfo
import liuliu.hotel.web.SoapObjectUtils
import liuliu.hotel.web.WebServiceUtils
import java.util.*

/**
 * Created by Administrator on 2017/4/18.
 */
interface IDown {
    fun downHotel(url: Boolean)
}

class LLogin(var down: IDown) {
    fun down(hotelId: String) {
        val map = HashMap<String, String>()
        map.put("lgdm", hotelId)
        WebServiceUtils.getMethod(1, "DownHotelEmployee", map) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, "DownHotelEmployee")
            if (model.isSuccess) {
                down.downHotel(true)
            }
        }
    }
}
