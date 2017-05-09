package liuliu.hotel.ui.activity

import android.app.Activity

import net.tsz.afinal.FinalDb

/**
 * Created by Administrator on 2017/4/24.
 */

class Test : Activity() {
    internal var db: FinalDb

    fun get(): String {
        db = FinalDb.create(this)
        var a = "AA-cc"
        a = a.replace("AA", "GGG")
        a = "aa" + a
        return a
    }
}
