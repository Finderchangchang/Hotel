package liuliu.hotel.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.CodeModel;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;
import liuliu.hotel.config.SaveKey;
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
    Dialog dialog;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_download_hotel);
        mListener = new DownHotelListener(this, finalDb);
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
    public void checkHotel(boolean result, final String mes) {
        dialog.dismiss();
        if (result) {//比对成功，登录页面.
            ToastShort("绑定成功，正在跳转。。。");
            Utils.IntentPost(LoginActivity.class);
            DownHotelActivity.this.finish();//关闭当前页面
        } else {
            finalDb.deleteAll(DBLGInfo.class);
            finalDb.deleteAll(CodeModel.class);
            ToastShort(mes);
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
                    dialog = Utils.ProgressDialog(this, dialog, "下载旅馆信息中，请稍候...", false);
                    dialog.show();
                    if (daima_et.getText().toString().trim().length() == 10) {
                        Utils.WriteString(SaveKey.KEY_Hotel_Id,daima_et.getText().toString().trim());
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
