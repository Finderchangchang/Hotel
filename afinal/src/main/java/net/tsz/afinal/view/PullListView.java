package net.tsz.afinal.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PullListView extends LinearLayout {
    private BaseAdapter adapter;
    private OnItemClickListener onItemClickListener = null;
    private int currentNum = -1;
    private boolean isFoot = false;

    public PullListView(Context context) {
        super(context);
        init();
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setAdapter(BaseAdapter adpater) {
        if (adpater == null) return;
        this.adapter = adpater;
        bindLinearLayout();
    }

    /**
     * addview
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        for (int i = currentNum + 1; i < count; i++) {
            final int a = i;
            View v = adapter.getView(i, null, null);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, a);
                }
            });
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            currentNum = i;
            addView(v, i);
        }
    }

    /**
     * 目前只能添加一个footer
     *
     * @param footerView
     */
    public void addFooterView(LinearLayout footerView) {
        // TODO Auto-generated method stub
        if (!isFoot) {
            addView(footerView, currentNum + 1);
            isFoot = true;
        }
    }

    public void removeFooterView() {
        if (isFoot) {
            removeViewAt(currentNum + 1);
            isFoot = false;
        }
    }

    public interface OnItemClickListener {
        void onClick(View v, int position);
    }

}
