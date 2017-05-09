package liuliu.hotel.method.inter

import liuliu.hotel.method.key
import liuliu.hotel.method.utils
import liuliu.hotel.model.DBTZTGInfo
import liuliu.hotel.utils.Utils
import liuliu.hotel.web.SoapObjectUtils
import liuliu.hotel.web.WebServiceUtils
import org.ksoap2.serialization.SoapObject
import java.util.*

/**
 * 首页逻辑处理
 * Created by Administrator on 2017/4/18.
 */
interface IMain {
    fun update(url: String)
}

class LMain(var view: IMain) {
    /**
     * 检查更新
     * */
    fun check_update() {
        var method = "GetMobileVersion"
        WebServiceUtils.getMethod(method) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, method)
            if (model.isSuccess) {
                if (model.obj.version.replace(".", "").toInt() > Utils.getVersion().replace(".", "").toInt()) {
                    view.update(model.obj.link)
                }
            }
        }
    }

    fun lian() {
        val map = HashMap<String, String>()
        map.put("lgdm", "1306010002")//如果建立资源，就返回true
        var method = "GetLGInfoByLGDM"
        WebServiceUtils.getMethod(method, map) { result ->
            val model = SoapObjectUtils.parseSoapObject(result, method)
            if (model.isSuccess) {

            }
        }
    }

    fun demo() {
        lian()
//        var method = "GetFH"
//        val map = HashMap<String, String>()
//        map.put("lgdm", "1306010002")
//        WebServiceUtils.getMethod(1, method, map) { result ->
//            val model = SoapObjectUtils.parseSoapObject(result, method)
//            if (model.isSuccess) {
//                if (model.obj.version.replace(".", "").toInt() > Utils.getVersion().replace(".", "").toInt()) {
//                    view.update(model.obj.link)
//                }
//            }
//        }
    }
}
