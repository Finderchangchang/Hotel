package liuliu.hotel.ui.activity

import android.content.Intent
import android.text.TextUtils
import gd.hotel.base.BActivity
import kotlinx.android.synthetic.main.activity_down.*

import liuliu.hotel.R
import liuliu.hotel.method.key
import liuliu.hotel.method.down
import liuliu.hotel.method.inter.IDown
import liuliu.hotel.method.inter.LLogin
import liuliu.hotel.method.utils
import liuliu.hotel.utils.Utils

class DownActivity : BActivity(), IDown {
    override fun downHotel(result: Boolean) {
        toast("url:")
    }

    override fun setLayout(): Int {
        return R.layout.activity_down
    }

    internal var login: LLogin = LLogin(this)
    override fun initViews() {
        down_hotel_btn.setOnClickListener {
            var pwd = pwd_et.text.toString().trim()
            var hotel_id = hotel_id_et.text.toString().trim()
            if (TextUtils.isEmpty(pwd)) {
                toast("管理密码不能为空")
            } else if (TextUtils.isEmpty(hotel_id)) {
                toast("旅馆代码不能为空")
            } else {
                if (down().check_pwd(pwd)) {
                    login.down(hotel_id)
//                    if (down().down_hotel(hotel_id)) {
//                        startActivity(Intent(this, MainsActivity::class.java))
//                        utils().putCache(key.hotel_id, hotel_id)
//                    } else {
//                        toast("下载失败，请重试")
//                    }
                } else {
                    toast("管理密码错误，请重新输入")
                }
            }
        }
        //如果旅馆数据存在跳转到首页
//        if (!TextUtils.isEmpty(utils().getCache(key.hotel_id))) {
//            startActivity(Intent(this, MainsActivity::class.java))
//        }
    }

    override fun initEvents() {

    }
}
