package liuliu.hotel.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;

/**
 * 人员详细信息
 * Created by Administrator on 2016/5/26.
 */
public class PersonDetailActivity extends BaseActivity {
    public static PersonDetailActivity mInstance;
    @CodeNote(id = R.id.toolbar)
    Toolbar toolbar;
    String serialId;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_person_detail);
    }

    @Override
    public void initEvents() {
        mInstance = this;
        serialId = getIntent().getStringExtra(Key.Person_Detail_SerialId);//获得流水号
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInstance.finish();
            }
        });
    }
}
