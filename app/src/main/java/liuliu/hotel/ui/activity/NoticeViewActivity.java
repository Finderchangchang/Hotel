package liuliu.hotel.ui.activity;

import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * Created by Administrator on 2016/6/4.
 */
public class NoticeViewActivity extends BaseActivity {
    @CodeNote(id=R.id.notice_title)TextView title;
    @CodeNote(id=R.id.notice_author)TextView authoer;
    @CodeNote(id=R.id.notice_createtime)TextView time;
    @CodeNote(id=R.id.notice_value)TextView value;


    @Override
    public void initViews() {
        setContentView(R.layout.view_notice);
    }

    @Override
    public void initEvents() {

    }
}
