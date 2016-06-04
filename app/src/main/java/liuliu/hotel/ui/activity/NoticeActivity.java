package liuliu.hotel.ui.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
    @CodeNote(id = R.id.notice_lv)
    ListView lv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_notice);
        mInstance = this;
        mListener = new NoticeListener(this, finalDb);
    }

    @Override
    public void initEvents() {
        setTitleBar("通知通告");
        mListener.request();
        //search_word_et.getText().toString().trim()
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cha_cancel_tv:
                mListener.request();
                //search_word_et.getText().toString().trim()
                break;
        }
    }

    @Override
    public void loadView(List<DBTZTGInfo> mList) {
        mAdapter = new CommonAdapter<DBTZTGInfo>(mInstance, mList, R.layout.item_notice) {
            @Override
            public void convert(ViewHolder holder, DBTZTGInfo dbtztgInfo, int position) {

                if (dbtztgInfo.getIsRead() == 1) {
                    holder.setImageResource(R.id.person_iv, R.mipmap.open_xf);
                } else {
                    holder.setImageResource(R.id.person_iv, R.mipmap.no_open_xf);
                }
                holder.setText(R.id.person_name_tv, dbtztgInfo.getFBR());
                holder.setText(R.id.address_tv, dbtztgInfo.getTGBT());
                holder.setText(R.id.notice_time, dbtztgInfo.getFBSJ());

            }
        };
        mAdapter.notifyDataSetChanged();
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


}
