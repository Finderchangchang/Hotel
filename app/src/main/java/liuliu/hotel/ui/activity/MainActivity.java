package liuliu.hotel.ui.activity


import android.app.FragmentTransaction
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import net.tsz.afinal.annotation.view.CodeNote
import net.tsz.afinal.model.ChangeItem
import net.tsz.afinal.model.ItemModel
import net.tsz.afinal.view.NormalDialog

import java.util.ArrayList

import liuliu.hotel.R
import liuliu.hotel.base.BaseActivity
import liuliu.hotel.ui.frag.MainFragment
import liuliu.hotel.ui.frag.SearchFragment
import liuliu.hotel.ui.frag.SettingFragment

/**
 * 首页
 */
class MainActivity : BaseActivity() {
    @CodeNote(id = R.id.main_frag)
    internal var frag_layout: FrameLayout? = null
    @CodeNote(id = R.id.main_ll, click = "onClick")
    internal var main_ll: LinearLayout? = null
    @CodeNote(id = R.id.search_ll, click = "onClick")
    internal var search_ll: LinearLayout? = null
    @CodeNote(id = R.id.setting_ll, click = "onClick")
    internal var setting_ll: LinearLayout? = null
    @CodeNote(id = R.id.main_iv)
    internal var main_iv: ImageView? = null
    @CodeNote(id = R.id.search_iv)
    internal var search_iv: ImageView? = null
    @CodeNote(id = R.id.setting_iv)
    internal var setting_iv: ImageView? = null
    @CodeNote(id = R.id.main_tv)
    internal var main_tv: TextView? = null
    @CodeNote(id = R.id.search_tv)
    internal var search_tv: TextView? = null
    @CodeNote(id = R.id.setting_tv)
    internal var setting_tv: TextView? = null
    internal var main_frag: MainFragment? = null
    internal var search_frag: SearchFragment? = null
    internal var setting_frag: SettingFragment? = null
    //底部菜单相关联
    internal var mItems: MutableList<ItemModel>
    internal var mClick = 0
    internal var listbtn: MutableList<ChangeItem>

    override fun initViews() {
        setContentView(R.layout.activity_main)
        mItems = ArrayList<ItemModel>()
        listbtn = ArrayList<ChangeItem>()
        mInstance = this
        llDialog = NormalDialog(this)
        val s = intent.getStringExtra("")
        val et = EditText(this)
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    override fun initEvents() {
        val s = arrayOfNulls<String>(3)
        mItems.add(ItemModel("首页", R.mipmap.main_normal, R.mipmap.main_pressed))
        mItems.add(ItemModel("搜索", R.mipmap.search_normal, R.mipmap.search_pressed))
        mItems.add(ItemModel("设置", R.mipmap.setting_normal, R.mipmap.setting_pressed))
        listbtn.add(ChangeItem(main_tv, main_iv))
        listbtn.add(ChangeItem(search_tv, search_iv))
        listbtn.add(ChangeItem(setting_tv, setting_iv))
        setItem(0)//显示首页

    }

    /**
     * 控件点击事件

     * @param view
     */
    fun onClick(view: View) {
        when (view.id) {
            R.id.main_ll -> setItem(0)
            R.id.search_ll -> setItem(1)
            R.id.setting_ll -> setItem(2)
        }
    }

    private fun setItem(position: Int) {
        //恢复成未点击状态
        listbtn[mClick].tv.setTextColor(resources.getColor(R.color.normal_lab))//正常字体颜色
        listbtn[mClick].iv.setImageResource(mItems[mClick].normal_img)
        //设置为点击状态
        listbtn[position].tv.setTextColor(resources.getColor(R.color.pressed_lab))//点击以后字体颜色
        listbtn[position].iv.setImageResource(mItems[position].pressed_img)
        mClick = position
        // 开启一个Fragment事务
        val transaction = fragmentManager.beginTransaction()
        if (main_frag != null) {
            transaction.hide(main_frag)
        }
        if (search_frag != null) {
            transaction.hide(search_frag)
        }
        if (setting_frag != null) {
            transaction.hide(setting_frag)
        }
        when (position) {
            0 -> if (main_frag == null) {
                // 如果MessageFragment为空，则创建一个并添加到界面上
                main_frag = MainFragment()
                transaction.add(R.id.main_frag, main_frag)
            } else {
                // 如果MessageFragment不为空，则直接将它显示出来
                transaction.show(main_frag)
            }
            1 -> if (search_frag == null) {
                // 如果MessageFragment为空，则创建一个并添加到界面上
                search_frag = SearchFragment()
                transaction.add(R.id.main_frag, search_frag)
            } else {
                // 如果MessageFragment不为空，则直接将它显示出来
                transaction.show(search_frag)
            }
            2 -> if (setting_frag == null) {
                // 如果MessageFragment为空，则创建一个并添加到界面上
                setting_frag = SettingFragment()
                transaction.add(R.id.main_frag, setting_frag)
            } else {
                // 如果MessageFragment不为空，则直接将它显示出来
                transaction.show(setting_frag)
            }
        }
        transaction.commit()
    }

    internal var llDialog: NormalDialog

    /**
     * 点击返回弹出消息框，确定后完全退出系统

     * @param keyCode key的编码
     * *
     * @param event   key的时间
     * *
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            llDialog.setMiddleMessage("确认要退出系统？")
            llDialog.setOnPositiveListener {
                //点击确定按钮
                llDialog.cancel()
                this@MainActivity.finish()//完全退出系统
            }
            llDialog.setOnNegativeListener { llDialog.cancel() }//点击取消按钮
            llDialog.show()
        }
        return false
    }

    companion object {
        var mInstance: MainActivity
    }
}
