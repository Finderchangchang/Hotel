package liuliu.hotel.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gd.hotel.base.BActivity

import liuliu.hotel.R
import liuliu.hotel.base.BaseActivity

/**
 * 无证登记
 * 先验证手机号，成功-
 * */
class AddNoCardctivity : BActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_add_no_cardctivity
    }

    override fun initViews() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initEvents() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 发送验证码
     * */
    fun sendTel() {

    }

    /**
     * 验证手机号是否正确
     * */
    fun checkTel(tel: String): Boolean {
        return true
    }
}
