package liuliu.hotel.ui.activity

import android.view.View
import android.widget.EditText

import liuliu.hotel.base.BaseActivity

/**
 * Created by Administrator on 2016/5/21.
 */
class LoadingActivity : BaseActivity() {
    override fun initViews() {

    }

    override fun initEvents() {
        val result = false
        val et = EditText(this)
        et.visibility = if (result) View.VISIBLE else View.GONE
    }
}
