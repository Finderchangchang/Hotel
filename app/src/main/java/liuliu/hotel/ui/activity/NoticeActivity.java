package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.utils.Utils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class NoticeActivity extends BaseActivity {
    public static NoticeActivity mInstance;
    @CodeNote(id = R.id.down_btn, click = "onClick")
    Button down_btn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_notice);
        mInstance = this;
    }

    @Override
    public void initEvents() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.down_btn:
                break;
        }
    }
}
