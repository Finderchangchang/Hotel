package liuliu.hotel.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.view.PullScrollView;
import net.tsz.afinal.view.RefreshView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.view.PersonAdapter;
import liuliu.hotel.view.VViewHolder;

/**
 * Created by Administrator on 2016/6/6.
 */
public class TestActivity extends BaseActivity {
    @CodeNote(id = R.id.rf_lv)
    RefreshView rf_lv;
    PersonAdapter<String> mAdapter;
    List<String> mList;

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
        rf_lv.setLayoutManager(new LinearLayoutManager(MainActivity.mInstance));
        mAdapter = new PersonAdapter<String>(mList, R.layout.item_test) {
            @Override
            public void convert(VViewHolder holder, String s, int position) {
                holder.setText(R.id.name_tv, s);
            }
        };
        rf_lv.setAdapter(mAdapter);
    }
}
