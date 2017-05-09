package liuliu.hotel.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.ivsign.android.IDCReader.CopyFile
import com.ivsign.android.IDCReader.Cvr100bMYTask
import com.ivsign.android.IDCReader.Cvr100bTask
import com.ivsign.android.IDCReader.IdcardInfoExtractor
import com.ivsign.android.IDCReader.IdcardValidator

import net.tsz.afinal.annotation.view.CodeNote
import net.tsz.afinal.model.CodeModel
import net.tsz.afinal.view.ImageEditText
import net.tsz.afinal.view.SpinnerDialog

import java.io.File
import java.util.ArrayList

import liuliu.hotel.R
import liuliu.hotel.base.BaseActivity
import liuliu.hotel.control.IDownHotelView
import liuliu.hotel.control.RegPersonListener
import liuliu.hotel.model.BlueToothModel
import liuliu.hotel.model.CustomerModel
import liuliu.hotel.model.PersonModel
import liuliu.hotel.utils.Utils
import liuliu.hotel.web.DBHelper

/**
 * 旅客入住
 * Created by Administrator on 2016/5/21.
 */
class AddPersonActivity : BaseActivity(), IDownHotelView {
    @CodeNote(id = R.id.save_btn, click = "onClick")
    internal var save_btn: Button? = null
    @CodeNote(id = R.id.user_img_iv, click = "onClick")
    internal var user_img_iv: ImageView? = null
    @CodeNote(id = R.id.user_name_iet)
    internal var user_name_iet: ImageEditText? = null//姓名
    @CodeNote(id = R.id.minzu_val_tv)
    internal var minzu_val_tv: TextView? = null//民族
    @CodeNote(id = R.id.xingbie_val_tv)
    internal var xingbie_val_tv: TextView? = null//性别
    @CodeNote(id = R.id.zhengjian_val_tv)
    internal var zhengjian_val_tv: TextView? = null//证件类型
    @CodeNote(id = R.id.minzu_ll, click = "onClick")
    internal var minzu_ll: LinearLayout? = null//证件
    @CodeNote(id = R.id.xingbie_ll, click = "onClick")
    internal var xingbie_ll: LinearLayout? = null//证件
    @CodeNote(id = R.id.zhengjian_ll, click = "onClick")
    internal var zhenjian_ll: LinearLayout? = null//证件
    @CodeNote(id = R.id.home_num_iet)
    internal var home_num_iet: ImageEditText? = null//房间号
    @CodeNote(id = R.id.idcard_iet)
    internal var idcard_iet: ImageEditText? = null//证件号
    @CodeNote(id = R.id.address_iet)
    internal var address_iet: ImageEditText? = null//地址
    internal var dialog: SpinnerDialog
    @CodeNote(id = R.id.reg_person_read, click = "onClick")
    internal var btnRead: Button? = null
    internal var path = StringBuffer()
    internal var listener: RegPersonListener
    internal var customerModel: CustomerModel
    internal var xbCode: MutableList<CodeModel>
    internal var MZcode: List<CodeModel> = ArrayList()
    internal var ZJLXcode: List<CodeModel> = ArrayList()
    internal var bm: Bitmap? = null

    override fun initViews() {
        setContentView(R.layout.activity_reg_person)
        setTitleBar("旅客登记")
        mInstance = this
        listener = RegPersonListener(this, this, finalDb)
        customerModel = CustomerModel()
        CopyFile.CopyWltlib(this)
    }

    override fun initEvents() {
        //        idcard_iet.AddChangeMethod(new EditChangedListener());
        ZJLXcode = finalDb.findAllByWhere(CodeModel::class.java, "CodeName='ZJLX'")
        //        zhengjian_val_tv.setText(ZJLXcode.get(0).getVal());
        //        customerModel.setCardType(ZJLXcode.get(0).getKey());
        MZcode = finalDb.findAllByWhere(CodeModel::class.java, "CodeName='MZ'")
        //        minzu_val_tv.setText(MZcode.get(0).getVal());
        //        customerModel.setNation(MZcode.get(0).getKey());
        xbCode = ArrayList<CodeModel>()
        xbCode.add(CodeModel("1", "男"))
        xbCode.add(CodeModel("2", "女"))
        xingbie_val_tv!!.text = "男"
        customerModel.sex = "1"//性别
        dialog = SpinnerDialog(this)
        dialog.setCanceledOnTouchOutside(true)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.save_btn -> if (checkInfo()) {
                getCustomerInfo()
                listener.addRZInfo(customerModel)
            }
            R.id.zhengjian_ll//证件照
            -> {
                dialog.setListView(ZJLXcode)
                dialog.show()
                dialog.setOnItemClick { position, model ->
                    zhengjian_val_tv!!.text = model.`val`
                    customerModel.cardType = model.key
                }
            }
            R.id.xingbie_ll//性别
            -> {
                dialog.setListView(xbCode)
                dialog.show()
                dialog.setOnItemClick { position, model ->
                    xingbie_val_tv!!.text = model.`val`
                    customerModel.sex = model.key//性别
                }
            }
            R.id.minzu_ll//民族
            -> {
                dialog.setListView(MZcode)
                dialog.show()
                dialog.setOnItemClick { position, model ->
                    minzu_val_tv!!.text = model.`val`
                    customerModel.nation = model.key
                }
            }
            R.id.reg_person_read ->
                //跳转到选择蓝牙设备页面
                //读取身份证
                if (Utils.ReadString("BlueToothAddress") == "") {
                    ToastShort("请检查蓝牙读卡设备设置！")
                } else {
                    onReadCardCvr()
                }
            R.id.user_img_iv -> startCamera(11)
        }
    }


    //开启拍照
    fun startCamera(type: Int) {
        // 利用系统自带的相机应用:拍照
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        path = StringBuffer()
        path.append(this@AddPersonActivity.getExternalFilesDir(null)).append("/header.jpg")
        val file = File(path.toString())
        val uri = Uri.fromFile(file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, type)
    }

    //cvr
    private fun onReadCardCvr() {
        if (Utils.checkBluetooth(this, 2)) {
            val progressDialog = ProgressDialog.show(this, "", "正在读取身份证信息...", true, false)
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    val bundle = msg.data
                    var find = false
                    progressDialog.dismiss()
                    if (bundle != null) {
                        val result = bundle.getBoolean("result")
                        val person = bundle.getSerializable("person_info") as PersonModel
                        if (result) {
                            find = true
                            setPerson(person)
                        } else {
                            if (null != person) {
                                find = true
                                ToastShort(person.personName)
                            }
                        }
                    }
                    if (!find) {
                        ToastShort("证件读取失败！（cvr）")
                    }
                }
            }
            Cvr100bMYTask().startCvr100bTask(this, object : Cvr100bTask.Cvr100bListener {
                override fun reauestBlueDevice(): BlueToothModel {
                    val blue = BlueToothModel()
                    blue.deviceAddress = Utils.ReadString("BlueToothAddress")
                    return blue
                }

                override fun onResult(result: Boolean, person: PersonModel) {
                    val msg = handler.obtainMessage(1)
                    val bundle = Bundle()
                    bundle.putBoolean("result", result)
                    bundle.putSerializable("person_info", person)
                    msg.data = bundle
                    msg.sendToTarget()
                }
            })
        } else {
            ToastShort("请检查蓝牙读卡设备设置！")
        }
    }

    private fun checkInfo(): Boolean {
        if (user_name_iet!!.text.trim { it <= ' ' } == "") {
            ToastShort("请填写姓名")
            return false
        } else if (idcard_iet!!.text.trim { it <= ' ' } == "") {
            ToastShort("请填写证件号")
            return false
        } else if (home_num_iet!!.text.trim { it <= ' ' } == "") {
            ToastShort("请填写房间号")
            return false
        } else if (address_iet!!.text.trim { it <= ' ' } == "") {
            ToastShort("请填写地址")
            return false
        } else if (!checkCardId()) {
            ToastShort("请核对证件号码")
            return false
        }
        return true
    }

    /**
     * 从界面获取值
     */
    private fun getCustomerInfo() {
        val card_num = idcard_iet!!.text
        customerModel.name = user_name_iet!!.text
        customerModel.cardId = idcard_iet!!.text
        //customerModel.setSex(xingbie_val_tv.getText().toString());
        customerModel.address = address_iet!!.text
        //customerModel.setNation(minzu_val_tv.getText().toString());
        customerModel.checkInTime = Utils.getNormalTime()
        customerModel.checkOutTime = ""
        if (card_num.length == 15 || card_num.length == 18) {
            customerModel.birthday = StringBuffer(card_num.substring(6, 14)).insert(4, "-").insert(7, "-").toString()
            customerModel.native = card_num.substring(0, 6)
        }
        customerModel.checkInSign = Utils.getVersionName()//当前系统版本
        if (bm != null) {
            customerModel.headphoto = bm
        } else {
            customerModel.headphoto = null
        }
        //customerModel.setHeadphoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        val num = DBHelper(finalDb, this).getSeralNum()
        customerModel.serialId = num
        customerModel.roomId = home_num_iet!!.text
        customerModel.checkInSign = versionName
    }

    //读卡以后界面赋值
    private fun setPerson(person: PersonModel) {
        user_name_iet!!.text = person.personName
        idcard_iet!!.text = person.personCardId
        xingbie_val_tv!!.text = ""
        address_iet!!.text = person.personAddress
        minzu_val_tv!!.text = person.personNation
        customerModel.birthday = person.personBirthday
        if (person.personCardImage != null) {
            if (person.personCardImage != "") {
                val bitmap = Utils.getBitmapByte(person.personCardImage)
                user_img_iv!!.setImageBitmap(bitmap)
                customerModel.url = person.personImgUrl
            }
        } else {
            ToastShort("照片解码失败！")
        }
    }

    override fun checkHotel(result: Boolean, mes: String) {
        ToastShort(mes)
        if (result) {
            setResult(-1)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == 11 && resultCode == -1) {
            bm = Utils.getimage(this, path.toString())
            // person_path = path.toString();
            if (bm != null) {
                bm = Utils.centerSquareScaleBitmap(bm, 60)
                user_img_iv!!.setImageBitmap(bm)
                user_img_iv!!.tag = bm
            } else {
                user_img_iv!!.setImageResource(R.mipmap.main_zhengjian)
                user_img_iv!!.tag = bm
            }
            //personModel.setPersonImgUrl(path.toString());
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 获取当前应用的版本号：
     */
    // 获取packagemanager的实例
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    val versionName: String
        get() {
            val Version = "[Version:num]-[Registe:Phone]"
            val packageManager = packageManager
            val packInfo: PackageInfo
            try {
                packInfo = packageManager.getPackageInfo(packageName, 0)
                val version = packInfo.versionName
                return Version.replace("num", version)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return Version.replace("num", "1.0")
        }

    internal inner class EditChangedListener : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            checkCardId()
        }
    }

    //根据民族KEY
    private fun getValue(key: String): String {
        for (code in MZcode) {
            if (code.key == key) {
                return code.`val`
            }
        }
        return ""
    }

    private fun checkCardId(): Boolean {
        var result = false
        try {
            val card_id = idcard_iet!!.text.toString()
            if (!Utils.isEmptyString(card_id)) {
                val iv = IdcardValidator()
                if (!iv.isValidatedAllIdcard(card_id)) {
                    idcard_iet!!.setCenterTextRedColor()
                } else {
                    val iie = IdcardInfoExtractor(card_id)
                    customerModel.sex = iie.gender
                    if (iie.gender == "1") {
                        xingbie_val_tv!!.text = "男"
                    } else {
                        xingbie_val_tv!!.text = "女"
                    }
                    //minzu_val_tv.setText(getValue(iie.getna));
                    //iie.getNativeCode()
                    customerModel.native = iie.nativeCode
                    customerModel.birthday = String.format("%04d-%02d-%02d",
                            iie.year, iie.month, iie.day)
                    result = true
                    idcard_iet!!.setCenterTextRedColor2()
                }
            }
        } catch (e: Exception) {
            idcard_iet!!.setCenterTextRedColor()
            result = false
        }

        return result
    }

    companion object {
        var mInstance: AddPersonActivity
    }
}
