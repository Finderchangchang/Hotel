package liuliu.hotel.ui.activity;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;

/**
 * 用户登录页面
 * Created by Administrator on 2016/5/19.
 */
public class LoginActivity extends BaseActivity {

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initEvents() {
        String hotel_name = getIntent().getStringExtra(Key.LOGIN_HOTEL_NAME);
    }
}
