package liuliu.hotel.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by linhonghong on 2015/11/13.
 */
public abstract class PtrrvBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected LayoutInflater mInflater;
    protected Context mContext = null;

    public PtrrvBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {

    }
//    public void setCount(int count){
//        mCount = count;
//    }


    public Object getItem(int position) {
        return null;
    }
}

