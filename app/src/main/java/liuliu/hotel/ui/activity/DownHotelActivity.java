package liuliu.hotel.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;
import liuliu.hotel.control.DownHotelListener;
import liuliu.hotel.control.IDownHotelView;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.utils.Utils;

/**
 * 下载旅馆代码页面
 * Created by Administrator on 2016/5/19.
 */
public class DownHotelActivity extends BaseActivity implements IDownHotelView {
    @CodeNote(id = R.id.daima_et)
    EditText daima_et;
    @CodeNote(id = R.id.down_btn, click = "onClick")
    Button down_btn;
    @CodeNote(id = R.id.code_tv)
    TextView code_tv;
    DownHotelListener mListener;
    String hotel_code = "";//绑定手机的随机码

    @Override
    public void initViews() {
        setContentView(R.layout.activity_download_hotel);
        mListener = new DownHotelListener(this,finalDb);
    }

    @Override
    public void initEvents() {
        hotel_code = Utils.getRandomChar(6);//生成随机码
        code_tv.setText(hotel_code);
    }

    /**
     * 获得比对结果
     *
     * @param result true，成功。
     * @param mes
     */
    @Override
    public void checkHotel(boolean result, String mes) {
        if (result) {//比对成功，登录页面.
            ToastShort("绑定成功，正在跳转。。。");

            Utils.IntentPost(LoginActivity.class, new Utils.putListener() {
                @Override
                public void put(Intent intent) {
                    intent.putExtra(Key.LOGIN_HOTEL_NAME, daima_et.getText().toString().trim());
                }
            });
            this.finish();//关闭当前页面
            //将model中的内容存储在本地
        } else {
            ToastShort("请认真核对验证码是否输入一致！！");
            daima_et.setEnabled(true);
            down_btn.setText("下载");
            down_btn.setEnabled(true);
        }
    }

    /**
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.down_btn:
                if (down_btn.getText().equals("下载")) {
                    if (daima_et.getText().toString().trim().length() == 10) {
                        daima_et.setEnabled(false);
                        down_btn.setEnabled(false);
                        down_btn.setText("重置");
                        mListener.pushCode(daima_et.getText().toString().trim(), hotel_code, Utils.getImei(), Utils.getPhoneNum(), Build.MODEL);
                    } else {
                        ToastShort("旅馆代码格式不正确，请重新输入！");
                        daima_et.setEnabled(true);
                        down_btn.setEnabled(true);
                        down_btn.setText("下载");
                    }
                } else {
                    daima_et.setEnabled(true);
                    down_btn.setText("下载");
                }
                break;
        }
    }
}
