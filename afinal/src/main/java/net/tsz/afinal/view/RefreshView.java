package net.tsz.afinal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.tsz.afinal.R;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RefreshView extends RecyclerView {
    private LinearLayout mTopL;
    private LinearLayout main_ll;
    private float startY;
    private int padding_top;

    public RefreshView(Context context) {
        super(context);
        init(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTopL = (LinearLayout) View.inflate(context, R.layout.item_top, null);
        main_ll = (LinearLayout) mTopL.findViewById(R.id.main_ll);
        mTopL.setMinimumHeight(70);
        addView(mTopL, 0);
        padding_top = mTopL.getMeasuredHeight();
        setPadding(mTopL.getPaddingLeft(), -70,
                mTopL.getPaddingRight(), mTopL.getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                setPadding(0, -70, 0, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = (int) ev.getY();
                int deltY = (int) (offsetY - startY);
                setPadding(0, deltY / 4, 0, 0);
                break;
        }
        return super.onTouchEvent(ev);
    }
}
