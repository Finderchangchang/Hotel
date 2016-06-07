package liuliu.hotel.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/6/7.
 */
public class VViewHolder extends RecyclerView.ViewHolder {
    View commonView;
    private int mPosition;
    private SparseArray<View> mViews;

    public VViewHolder(View itemView) {
        super(itemView);
        commonView = itemView;
        this.mViews = new SparseArray<>();
    }

    public static VViewHolder get(View convertView, int layoutId, int position) {
        if (convertView == null) {
            return new VViewHolder(convertView);
        } else {
            VViewHolder holder = (VViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = commonView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public VViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
}
