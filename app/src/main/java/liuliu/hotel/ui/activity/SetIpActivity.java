package liuliu.hotel.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.utils.Utils;

/**
 * 设置IP
 * Created by Administrator on 2016/5/19.
 */
public class SetIpActivity extends BaseActivity {
    @CodeNote(id = R.id.toolbar)
    Toolbar toolbar;
    @CodeNote(id = R.id.center_title_tv, text = "网络设置")
    TextView center_title_tv;
    @CodeNote(id = R.id.ip_et, hint = "请输入IP地址")
    EditText ip_et;
    @CodeNote(id = R.id.duankou_et, hint = "请输入端口")
    EditText duankou_et;
    @CodeNote(id = R.id.save_btn, click = "onClick")
    Button save_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_set_ip);
    }

    @Override
    public void initEvents() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Utils.ReadString(SaveKey.KEY_IP) != "" && Utils.ReadString(SaveKey.KEY_PORT) != "") {
            checkPost();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                if (checkVal()) {
                    Utils.WriteString(SaveKey.KEY_IP, ip_et.getText().toString());
                    Utils.WriteString(SaveKey.KEY_PORT, duankou_et.getText().toString());
                    checkPost();
                }
                break;
        }
    }

    /**
     * 检验输入内容是否为空，格式正确
     *
     * @return
     */
    private boolean checkVal() {
        String ip = ip_et.getText().toString().trim();
        String port = duankou_et.getText().toString().trim();
        if (ip == "") {
            ToastShort("IP地址不能为空");
            return false;
        } else {
            if (!Utils.checkIP(ip)) {
                ToastShort("IP地址格式不正确，请重新输入");
                return false;
            }
        }
        if (port == "") {
            ToastShort("端口不能为空");
            return false;
        }
        return true;
    }

    /**
     * 检测跳页
     */
    private void checkPost() {
        if (finalDb.findAll(DBLGInfo.class).size() > 0) {//存在旅馆信息跳转到登陆页面
            Utils.IntentPost(LoginActivity.class);
        } else {//不存在旅馆信息跳转到配置旅馆代码页面
            Utils.IntentPost(DownHotelActivity.class);
        }
        this.finish();
    }
}
