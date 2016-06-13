package liuliu.hotel.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;
import liuliu.hotel.config.SaveKey;
import liuliu.hotel.control.INoticeView;
import liuliu.hotel.control.LoginListener;
import liuliu.hotel.control.NoticeListener;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.DBTZTGInfo;
import liuliu.hotel.utils.Utils;

/**
 * 用户登录页面
 * Created by Administrator on 2016/5/19.
 */
public class LoginActivity extends BaseActivity implements INoticeView {
    public static LoginActivity mInstance;
    @CodeNote(id = R.id.hotel_name_et)
    EditText hotel_name_et;
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;
    @CodeNote(id = R.id.back_setting_iv, click = "onClick")
    ImageView back_setting_iv;
    LoginListener listener;
    Dialog dialog;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
        mInstance = this;
        listener = new LoginListener(this, finalDb);
    }

    @Override
    public void initEvents() {
        hotel_name_et.setText(Utils.ReadString(SaveKey.KEY_Hotel_Name));
        hotel_name_et.setEnabled(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                dialog = Utils.ProgressDialog(this, dialog, "登录中，请稍候...", false);
                dialog.show();
                List<DBLGInfo> list = finalDb.findAllByWhere(DBLGInfo.class, "LGMC='" + hotel_name_et.getText() + "' and LoginPwd='" + pwd_et.getText().toString().trim() + "'");
                if (list.size() > 0) {
                    listener.request();
                } else {
                    dialog.dismiss();
                    ToastShort("密码错误，请重新输入");
                }
                break;
            case R.id.back_setting_iv:
                Utils.IntentPost(SetIpActivity.class, new Utils.putListener() {
                    @Override
                    public void put(Intent intent) {
                        intent.putExtra(Key.Reg_IP_Port, "login");//跳转到设置ip端口页面
                    }
                });
                break;
        }
    }

    @Override
    public void loadView(List<DBTZTGInfo> mList) {
        dialog.dismiss();
        ToastShort("登录成功！");
        Utils.IntentPost(MainActivity.class);
        this.finish();
    }
}
