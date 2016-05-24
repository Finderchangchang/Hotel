package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.utils.Utils;

/**
 * 用户登录页面
 * Created by Administrator on 2016/5/19.
 */
public class LoginActivity extends BaseActivity {
    @CodeNote(id = R.id.hotel_name_et)
    EditText hotel_name_et;
    @CodeNote(id = R.id.pwd_et)
    EditText pwd_et;
    @CodeNote(id = R.id.login_btn, click = "onClick")
    Button login_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initEvents() {
        String hotel_name = getIntent().getStringExtra(Key.LOGIN_HOTEL_NAME);//旅馆名称
        if (hotel_name != null) {
            hotel_name_et.setText(hotel_name);
            hotel_name_et.setEnabled(false);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                List<DBLGInfo> list = finalDb.findAllByWhere(DBLGInfo.class, "LGMC='" + hotel_name_et.getText() + "' and LoginPwd='" + pwd_et.getText().toString().trim() + "'");
                if (list.size() > 0) {
                    ToastShort("登录成功！");
                    Utils.IntentPost(MainActivity.class);
                } else {
                    ToastShort("密码错误，请重新输入");
                }
                break;
        }
    }
}
