package gd.hotel.base

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast

/**
 * BaseActivity声明相关通用方法
 *
 *
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
abstract class BActivity : AppCompatActivity() {
    internal var layoutid: Int = 0
    internal var dialog: ProgressDialog? = null
    var builder: AlertDialog.Builder? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutid = setLayout()
        if (layoutid != 0) {
            setContentView(layoutid)
        }
        builder = AlertDialog.Builder(this)
        initViews()
        initEvents()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()//左侧点击关闭当前页面
        }
        return true
    }

    abstract fun setLayout(): Int

    abstract fun initViews()

    abstract fun initEvents()


    private var toast: Toast? = null

    fun toast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }
}
