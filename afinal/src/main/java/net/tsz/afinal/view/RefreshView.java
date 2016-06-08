package net.tsz.afinal.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.tsz.afinal.R;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RefreshView extends RecyclerView {
    private LinearLayout mTopL;

    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        mTopL = (LinearLayout) View.inflate(context, R.layout.item_top, null);
        addView(mTopL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("Tag", "startY:" + ev.getY());
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("Tag", "move:" + ev.getY());
                break;
        }
        return super.onTouchEvent(ev);
    }
}
