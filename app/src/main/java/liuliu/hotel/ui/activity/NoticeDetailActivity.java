package liuliu.hotel.ui.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.DBTZTGInfo;

/**
 * Created by Administrator on 2016/6/4.
 */
public class NoticeDetailActivity extends BaseActivity {
    @CodeNote(id = R.id.notice_title)
    TextView title;
    @CodeNote(id = R.id.notice_author)
    TextView authoer;
    @CodeNote(id = R.id.notice_createtime)
    TextView time;
    @CodeNote(id = R.id.notice_value)
    TextView value;
    @CodeNote(id = R.id.notice_fanwei)
    TextView fanwei;

    DBTZTGInfo info = null;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_notice_detail);
        info = (DBTZTGInfo) getIntent().getSerializableExtra("NoticeModel");
    }

    @Override
    public void initEvents() {
        setTitleBar("通知通告");
        if (null != info) {
            title.setText(info.getTGBT());
            authoer.setText(info.getFBR());
            time.setText(info.getFBSJ());
            value.setText((Html.fromHtml(info.getTGNR())));
            fanwei.setText(info.getTZFW());
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(123);
                finish();
            }
        });
    }
}
