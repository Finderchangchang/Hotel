package liuliu.hotel.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseApplication;

/**
 * Created by Administrator on 2016/6/7.
 */
public abstract class PersonAdapter<T> extends RecyclerView.Adapter<VViewHolder> {
    private int layoutId;
    private List<T> list;

    public PersonAdapter(List mList, int mLayoutId) {
        layoutId = mLayoutId;
        list = mList;
    }


    @Override
    public VViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(layoutId, null);
        return new VViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VViewHolder holder, int position) {
        convert(holder, list.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public void setVal(List<T> newList) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (newList != null) {
            for (T model : newList) {
                list.add(model);
            }
        }
    }

    public abstract void convert(VViewHolder holder, T t, int position);

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.person_name_tv);
        }
    }
}
