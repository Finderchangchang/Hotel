package liuliu.hotel.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
import liuliu.hotel.utils.Utils;

/**
 * Created by Administrator on 2016/5/30.
 */
public class NoticeActivity extends BaseActivity{
    public static NoticeActivity mInstance;
    @CodeNote(id = R.id.search_word_et)
    EditText search_word_et;//查询条件
    @CodeNote(id = R.id.cha_cancel_tv, click = "onClick")
    TextView cha_cancel_tv;
    CommonAdapter<DBTZTGInfo> mAdapter;
    @CodeNote(id = R.id.notice_lv)
    ListView lv;
    List<DBTZTGInfo> list = null;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_notice);
        mInstance = this;

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(NoticeActivity.this, NoticeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NoticeModel", list.get(position));
                DBTZTGInfo info=list.get(position);
                info.setIsRead(1);
                finalDb.update(info);
                list.set(position,info);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
        });
    }

    @Override
    public void initEvents() {
        list = finalDb.findAll(DBTZTGInfo.class);
        setTitleBar("通知通告");
        load();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cha_cancel_tv:
                String title = search_word_et.getText().toString().trim();
                list = finalDb.findAllByWhere(DBTZTGInfo.class, "TGBT like '%" + title + "%'");
                load();
                break;
        }
    }

    public void load() {
        mAdapter = new CommonAdapter<DBTZTGInfo>(mInstance, list, R.layout.item_notice) {
            @Override
            public void convert(ViewHolder holder, DBTZTGInfo dbtztgInfo, int position) {
                if (dbtztgInfo.getIsRead() == 1) {
                    holder.setImageResource(R.id.person_iv, R.mipmap.open_xf);
                } else {
                    holder.setImageResource(R.id.person_iv, R.mipmap.no_open_xf);
                }
                if (dbtztgInfo.getTGBT().length() > 13) {
                    holder.setText(R.id.notict_item_title, dbtztgInfo.getTGBT().substring(0, 12));
                } else {
                    holder.setText(R.id.notict_item_title, dbtztgInfo.getTGBT());
                }
                holder.setText(R.id.notict_item_content, dbtztgInfo.getFBR());
                holder.setText(R.id.notice_time, dbtztgInfo.getFBSJ());
            }
        };
        mAdapter.notifyDataSetChanged();
        lv.setAdapter(mAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 123) {
            load();
        }
    }
}
