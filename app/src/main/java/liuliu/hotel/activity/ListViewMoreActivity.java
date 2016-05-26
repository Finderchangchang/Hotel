package liuliu.hotel.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.AUtils;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;
import net.tsz.afinal.view.LoadingMoreListView;
import net.tsz.afinal.view.OnScrollListViewMore;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ListViewMoreActivity extends BaseActivity {
    @CodeNote(id = R.id.list_more_lv)
    LoadingMoreListView lmv;
    CommonAdapter<String> adapter = null;
    TextView loadMore;
    View footer;

    List<String> list = new ArrayList<>();

    @Override
    public void initViews() {
        setContentView(R.layout.activity_list_more);
        final List<String> list = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            list.add(i - 1, "" + i);
        }
        footerView();
        final List<String> mylist = list;
        OnScrollListViewMore myMore = new OnScrollListViewMore() {
            @Override
            public void scrollBottomState(int index) {
                mylist.clear();
                for (int i = 1; i < 25; i++) {
                    mylist.add(i + "");
                }
                loadMore(index);
            }
        };
        lmv = new LoadingMoreListView(this, null, myMore);
        lmv.addFooterView(footer);
    }

    private void loadMore(int index) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new CommonAdapter<String>(this, list, R.layout.item_bluthe) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.blue_name, s);
            }
        };
        lmv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lmv.setSelection(index);
        loadMore.setText("加载更多");
        //lmv.setAdapter(adapter);
    }

    private void footerView() {
        footer = LayoutInflater.from(this).inflate(net.tsz.afinal.R.layout.view_list_more, null);
        loadMore = (TextView) footer.findViewById(net.tsz.afinal.R.id.list_more_mes);
    }

    @Override
    public void initEvents() {
        list.clear();
        //list=new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            list.add(i + "");
        }
        adapter = new CommonAdapter<String>(this, list, R.layout.item_bluthe) {
            @Override
            public void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.blue_name, s);
            }
        };
        lmv.setAdapter(adapter);
        AUtils.setListViewHeight(lmv);
    }


}
