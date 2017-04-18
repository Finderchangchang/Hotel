package liuliu.hotel.method

import android.content.Context
import liuliu.hotel.base.BaseApplication
import rx.Observable
import java.util.*

/**
 * Created by Administrator on 2017/4/17.
 */
class utils{
    fun putCache(map: Map<String, String>) {
        val sp = BaseApplication.context!!.getSharedPreferences("deliver", Context.MODE_PRIVATE)
        val editor = sp.edit()
        Observable.from(map.keys)
                .subscribe { `val` -> editor.putString(`val`, map[`val`]) }
        editor.commit()
    }

    fun putBooleanCache(key: String, result: Boolean) {
        val sp = BaseApplication.context!!.getSharedPreferences("deliver", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(key, result)
        editor.commit()
    }

    fun putCache(key: String, `val`: String) {
        val map = HashMap<String, String>()
        map.put(key, `val`)
        putCache(map)
    }

    fun getCache(key: String): String {
        val sharedPreferences = BaseApplication.context!!.getSharedPreferences("deliver", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun getBooleanCache(key: String): Boolean {
        val sharedPreferences = BaseApplication.context!!.getSharedPreferences("deliver", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }
}