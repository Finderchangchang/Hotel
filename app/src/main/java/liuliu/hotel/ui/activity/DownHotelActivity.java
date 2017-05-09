package liuliu.hotel.ui.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import net.tsz.afinal.annotation.view.CodeNote
import net.tsz.afinal.model.CodeModel
import net.tsz.afinal.utils.CommonAdapter
import net.tsz.afinal.utils.ViewHolder

import liuliu.hotel.R
import liuliu.hotel.base.BaseActivity
import liuliu.hotel.config.Key
import liuliu.hotel.config.SaveKey
import liuliu.hotel.control.DownHotelListener
import liuliu.hotel.control.IDownHotelView
import liuliu.hotel.model.DBLGInfo
import liuliu.hotel.utils.Utils

/**
 * 下载旅馆代码页面
 * Created by Administrator on 2016/5/19.
 */
class DownHotelActivity : BaseActivity(), IDownHotelView {
    @CodeNote(id = R.id.daima_et)
    internal var daima_et: EditText? = null
    @CodeNote(id = R.id.down_btn, click = "onClick")
    internal var down_btn: Button? = null
    @CodeNote(id = R.id.code_tv)
    internal var code_tv: TextView? = null
    @CodeNote(id = R.id.back_setting_iv, click = "onClick")
    internal var setting: ImageView? = null
    internal var mListener: DownHotelListener
    internal var hotel_code = ""//绑定手机的随机码
    internal var dialog: Dialog

    override fun initViews() {
        setContentView(R.layout.activity_download_hotel)
        mListener = DownHotelListener(this, finalDb)
    }

    override fun initEvents() {
        hotel_code = Utils.getRandomChar(6)//生成随机码
        code_tv!!.text = hotel_code
    }

    private fun load() {
        val items = arrayOf("张三", "李四", "王五")
        //dialog参数设置
        val builder = AlertDialog.Builder(this)  //先得到构造器
        builder.setTitle("提示") //设置标题
        //builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher)//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items) { dialog, which -> dialog.dismiss() }
        builder.setPositiveButton("确定") { dialog, which -> dialog.dismiss() }
        builder.create().show()
    }

    /**
     * 获得比对结果

     * @param result true，成功。
     * *
     * @param mes
     */
    override fun checkHotel(result: Boolean, mes: String) {
        dialog.dismiss()
        if (result) {//比成功，登录页面.
            Utils.IntentPost(LoginActivity::class.java)
            this@DownHotelActivity.finish()//关闭当前页面
        } else {
            finalDb.deleteAll(DBLGInfo::class.java)
            finalDb.deleteAll(CodeModel::class.java)
            if (mes == "null") {
                ToastShort("请检查ip端口是否正确！！")
            } else {
                ToastShort(mes)
            }
            daima_et!!.isEnabled = true
            down_btn!!.text = "下载"
            down_btn!!.isEnabled = true
        }
    }

    /**
     * @param view
     */
    fun onClick(view: View) {
        when (view.id) {
            R.id.down_btn -> if (down_btn!!.text == "下载") {

                if (daima_et!!.text.toString().trim { it <= ' ' }.length == 10) {
                    dialog = Utils.ProgressDialog(this, dialog, "第一次配置较慢，请耐心等待。。。", false)
                    dialog.show()
                    Utils.WriteString(SaveKey.KEY_Hotel_Id, daima_et!!.text.toString().trim { it <= ' ' })
                    daima_et!!.isEnabled = false
                    down_btn!!.isEnabled = false
                    down_btn!!.text = "重置"
                    mListener.pushCode(daima_et!!.text.toString().trim { it <= ' ' }, hotel_code, Utils.getImei(), Utils.getPhoneNum(), Build.MODEL)
                } else {
                    ToastShort("旅馆代码格式不正确，请重新输入！")
                    daima_et!!.isEnabled = true
                    down_btn!!.isEnabled = true
                    down_btn!!.text = "下载"
                }
            } else {
                daima_et!!.isEnabled = true
                down_btn!!.text = "下载"
            }
            R.id.back_setting_iv -> Utils.IntentPost(SetIpActivity::class.java) { intent ->
                intent.putExtra(Key.Reg_IP_Port, "down")//跳转到设置ip端口页面
            }
        }
    }
}
