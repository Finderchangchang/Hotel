package liuliu.hotel.method.inter

import liuliu.hotel.method.key
import liuliu.hotel.method.utils
import liuliu.hotel.model.DBLGInfo
import liuliu.hotel.model.DBTZTGInfo
import liuliu.hotel.web.SoapObjectUtils
import liuliu.hotel.web.WebServiceUtils
import net.tsz.afinal.FinalDb
import org.ksoap2.serialization.SoapObject
import java.util.*

/**
 * 下载旅馆代码以及相关配置信息
 * Created by Administrator on 2017/4/18.
 */
interface IDown {
    /**
     * 下载缓存配置信息结果
     * */
    fun downConfigResult(result: Boolean)
}

class LDown(var down: IDown, var db: FinalDb) {
    fun test() {
        val properties = HashMap<String, String>()
        properties.put("lgdm", "1306010011")//如果建立资源，就返回true
        properties.put("qyscm","20F64-571E0-536B5-EB219-A6542")
        WebServiceUtils.callWebService(true, "RequestServerSource", properties) { result ->
            if (null != result) {
                val invokeReturn = SoapObjectUtils.parseSoapObject(result, "RequestServerSource")
                println("DisposeServerSource" + result)
                if (invokeReturn.isSuccess) {
                    //ToastShort("下载成功");
                } else {
                    //ToastShort("下载失败");
                }
            } else {
                //ToastShort("下载失败");
            }
        }
    }

    /**
     * 下载旅馆信息
     * id:旅馆代码
     * */
    fun downHotel(id: String) {
        var method = "MobileGetLGInfoByLGDM"
        val map = HashMap<String, String>()
        map.put("lgdm", id)
//        WebServiceUtils.getMethod(method, map) { result ->
//            var model = SoapObjectUtils.parseSoapObject(result, method)
//            if (model.isSuccess) {
//                db.deleteAll(DBTZTGInfo::class.java)
//                db.save(model.data[0])
//                downRooms(true, id)//下载
//            } else {
//                down.downConfigResult(false)
//            }
//        }

    }

    /**
     * 下载房号 true,也下载从业人员
     * */
    fun downRooms(value: Boolean, id: String) {
        var method = "GetFH"
        val map = HashMap<String, String>()
        map.put("lgdm", id)
//        WebServiceUtils.getMethod(method, map) { result ->
//            val model = SoapObjectUtils.parseSoapObject(result, method)
//            utils().putBooleanCache(key.save_room, model.isSuccess)
//            if (value) {//成功
//                downEmployee(id)
//            }
//        }
    }

    /**
     * 下载从业人员
     * */
    fun downEmployee(id: String) {
        var method = "DownHotelEmployee"
        val map = HashMap<String, String>()
        map.put("lgdm", id)
//        WebServiceUtils.getMethod(method, map) { result ->
//            val model = SoapObjectUtils.parseSoapObject(result, method)
//            utils().putBooleanCache(key.save_people, model.isSuccess)
//            if (model.isSuccess) {
//
//            }
//            down.downConfigResult(true)
//        }
    }

}
