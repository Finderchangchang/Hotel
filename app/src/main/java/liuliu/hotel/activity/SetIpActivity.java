package liuliu.hotel.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * 设置IP
 * Created by Administrator on 2016/5/19.
 */
public class SetIpActivity extends BaseActivity {
    @CodeNote(id = R.id.center_title_tv, text = "网络设置")
    TextView center_title_tv;
    @CodeNote(id = R.id.ip_et, hint = "请输入IP地址")
    EditText ip_et;
    @CodeNote(id = R.id.duankou_et, hint = "请输入端口")
    EditText duankou_et;
    @CodeNote(id = R.id.save_btn, click = "onClick")
    Button save_btn;
    @CodeNote(id = R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_set_ip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                break;
        }
    }
}
