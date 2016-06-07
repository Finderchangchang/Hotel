package liuliu.hotel.ui.activity;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;

import net.tsz.afinal.annotation.view.CodeNote;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;

/**
 * Created by Administrator on 2016/6/6.
 */
public class TestActivity extends BaseActivity {
    @CodeNote(id = R.id.ptrrv)
    PullToRefreshRecyclerView ptrrv;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initEvents() {
        BaseLoadMoreView loadMoreView = new BaseLoadMoreView(this, ptrrv.getRecyclerView());
        loadMoreView.setLoadmoreString("加载哈哈");
        loadMoreView.setLoadMorePadding(100);

        ptrrv.setLoadMoreFooter(loadMoreView);
    }
}
