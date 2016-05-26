package net.tsz.afinal.view;

import android.content.Context;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import net.tsz.afinal.R;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ListViewMoreView extends ListView implements AbsListView.OnScrollListener{
    Context mycontext;
    OnScrollListViewMore listViewMore;
    int visibileItemIndex = 0;//最后的可视项索引


    public ListViewMoreView(Context context) {
        super(context);
        mycontext = context;

    }

    public ListViewMoreView(Context context, AttributeSet attributeSet, OnScrollListViewMore more) {
        super(context, attributeSet);
        listViewMore = more;

    }

    public ListViewMoreView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mycontext = context;

    }

    @Override
    public void addFooterView(View v) {
        super.addFooterView(v);
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int lastIndex = getCount();
        //当滑动到底部时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibileItemIndex>=lastIndex) {
            listViewMore.scrollBottomState(visibileItemIndex);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //visibleitemCount表示当前屏幕可以见到的ListItem（部分显示的ListItem也算）总数
        //firstVisibleItem表示当前屏幕可见的第一个ListItem（部分显示的ListItem也算)在整个LiSTivE的位置（下标0开始)
        this.visibileItemIndex = firstVisibleItem+visibleItemCount;
    }

}
