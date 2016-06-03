package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;

import java.util.HashMap;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.control.INoticeView;
import liuliu.hotel.control.NoticeListener;
import liuliu.hotel.model.DBTZTGInfo;

/**
 * Created by Administrator on 2016/5/30.
 */
public class NoticeActivity extends BaseActivity implements INoticeView {
    public static NoticeActivity mInstance;
    @CodeNote(id = R.id.search_word_et)
    EditText search_word_et;//查询条件
    @CodeNote(id = R.id.cha_cancel_tv, click = "onClick")
    TextView cha_cancel_tv;
    CommonAdapter<DBTZTGInfo> mAdapter;
    NoticeListener mListener;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_notice);
        mInstance = this;
        mListener = new NoticeListener(this);
    }

    @Override
    public void initEvents() {
        setTitleBar("通知通告");
        mListener.searchWord(search_word_et.getText().toString().trim());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cha_cancel_tv:
                mListener.searchWord(search_word_et.getText().toString().trim());
                break;
        }
    }

    @Override
    public void loadView(List<DBTZTGInfo> mList) {
        mAdapter = new CommonAdapter<DBTZTGInfo>(mInstance, mList, 0) {
            @Override
            public void convert(ViewHolder holder, DBTZTGInfo dbtztgInfo, int position) {
                
            }
        };
    }
}
