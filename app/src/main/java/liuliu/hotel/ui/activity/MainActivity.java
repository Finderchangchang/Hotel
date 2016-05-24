package liuliu.hotel.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    Toolbar toolbar;
    @CodeNote(id = R.id.ruzhu_ll, click = "onClick")
    LinearLayout ruzhu_ll;
    @CodeNote(id = R.id.chaxun_ll, click = "onClick")
    LinearLayout chaxun_ll;
    @CodeNote(id = R.id.shezhi_ll, click = "onClick")
    LinearLayout shezhi_ll;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initEvents() {

    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ruzhu_ll://人员添加
                break;
            case R.id.chaxun_ll://人员查询
                break;
            case R.id.shezhi_ll://设置管理
                break;
        }
    }
}
