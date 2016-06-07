package liuliu.hotel.view;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tsz.afinal.utils.ViewHolder;

import java.util.List;

import liuliu.hotel.R;

public abstract class RyAdapter<T> extends RecyclerView.Adapter<VViewHolder> {
    private List<T> mMessages;
    private int mLayoutId;
    private List<T> mData;

    public RyAdapter(Context context, List<T> messages, int layoutId) {
        mMessages = messages;
        mLayoutId = layoutId;
        mData = messages;
    }

    /**
     * 组件绑定
     *
     * @param parent
     * @return
     */
    @Override
    public VViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new VViewHolder(v);
    }

    /**
     * 数据与组件绑定
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(VViewHolder viewHolder, int position) {
        convert(viewHolder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public abstract void convert(VViewHolder holder, T t, int position);
}
