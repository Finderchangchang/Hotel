package liuliu.hotel.method

import liuliu.hotel.utils.Utils
import liuliu.hotel.web.SoapObjectUtils
import liuliu.hotel.web.WebServiceUtils
import org.ksoap2.serialization.SoapObject
import java.util.*

/**
 * Created by Administrator on 2017/4/17.
 */
class down {
    /**
     * 验证管理密码jcecc月加日
     * */
    fun check_pwd(pwd: String): Boolean {
        var c = Calendar.getInstance()
        var month = (c.get(Calendar.MONTH) + 1)
        var day = c.get(Calendar.DAY_OF_MONTH)
        var result = month + day
        if (day.toString().length == 2) {//天数是2位
            if (month.toString().length == 2) {
                var end_num = day.toString().substring(1).toInt() + month.toString().substring(1).toInt()
                if (end_num >= 10) {
                    result -= 10
                }
            } else {
                var end_num = day.toString().substring(1).toInt() + month
                if (end_num >= 10) {
                    result -= 10
                }
            }
        } else {//天数为1位
            if (month.toString().length == 2) {
                var end_num = day + month.toString().substring(1).toInt()
                if (end_num >= 10) {
                    result -= 10
                }
            } else {
                if (result >= 10) {
                    result -= 10
                }
            }
        }
        return pwd.toInt() == result
    }

    /**
     * 检测是否更新
     * */
    fun checkRefresh() {
        var url = ""
        WebServiceUtils.callWebService("GetMobileVersion") { result ->
            val model = SoapObjectUtils.parseSoapObject(result, "GetMobileVersion")
            if (model.isSuccess) {
                if (model.obj.version.replace(".", "").toInt() > Utils.getVersion().replace(".", "").toInt()) {
                    model.obj.link
                }
            } else {

            }
        }
    }

    /**
     * 根据旅馆ID下载旅馆信息
     * */
    fun down_hotel(hotel: String): Boolean {
        WebServiceUtils.callWebService("GetMobileVersion") { result ->
            val model = SoapObjectUtils.parseSoapObject(result, "GetMobileVersion")
            if (model.isSuccess) {

            }
        }
        return true
    }
}