package liuliu.hotel.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * 人员详细信息
 * Created by Administrator on 2016/5/26.
 */
public class PersonDetailsActivity extends BaseActivity {
    public static PersonDetailsActivity mInstance;
    @CodeNote(id = R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_person_detail);
    }

    @Override
    public void initEvents() {
        mInstance = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
