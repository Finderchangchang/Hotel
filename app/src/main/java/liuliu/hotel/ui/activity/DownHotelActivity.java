package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * 下载旅馆代码页面
 * Created by Administrator on 2016/5/19.
 */
public class DownHotelActivity extends BaseActivity {
    @CodeNote(id = R.id.daima_et)
    EditText daima_et;
    @CodeNote(id = R.id.down_btn, click = "onClick")
    Button down_btn;
    @CodeNote(id = R.id.code_tv)
    TextView code_tv;
    @CodeNote(id = R.id.code_ll)
    LinearLayout code_ll;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_download_hotel);
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.down_btn:
                if (daima_et.getText().toString().trim().length() == 10) {
                    //生成随机码
                }
                break;
        }
    }
}
