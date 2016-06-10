package liuliu.hotel.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * Created by Administrator on 2016/6/6.
 */
public class TestActivity extends BaseActivity implements RefreshListView.OnRefreshListener {
    @CodeNote(id = R.id.rf_lv)
    RefreshListView rf_lv;
    CommonAdapter<String> mAdapter;
    List<String> mList;
    Button mBtn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initEvents() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("i:" + i);
        }
        mAdapter = new CommonAdapter<String>(this, mList, R.layout.item_test) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.name_tv, s);
            }
        };
        View view = View.inflate(this, R.layout.item_top, null);
        mBtn = (Button) view.findViewById(R.id.btn);
            mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastShort("vv");
            }
        });
        rf_lv.addHeaderView(view);
        rf_lv.setAdapter(mAdapter);
        rf_lv.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final static int REFRESH_COMPLETE = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    rf_lv.refreshComplete();
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
