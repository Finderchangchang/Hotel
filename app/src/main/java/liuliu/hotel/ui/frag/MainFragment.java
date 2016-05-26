package liuliu.hotel.ui.frag;

import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.utils.CommonAdapter;
import net.tsz.afinal.utils.ViewHolder;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseFragment;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.ui.activity.MainActivity;
import liuliu.hotel.utils.Utils;

/**
 * 首页详细内容
 * Created by Administrator on 2016/5/24.
 */
public class MainFragment extends BaseFragment {
    @CodeNote(id = R.id.add_person_btn, click = "onClick")
    Button add_person_btn;
    CommonAdapter<PersonModel> mAdapter;

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
    }

    @Override
    public void initEvents() {
        mAdapter = new CommonAdapter<PersonModel>(MainActivity.mInstance, null, R.layout.item_person) {
            @Override
            public void convert(ViewHolder holder, PersonModel personModel, int position) {

            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_person_btn://点击添加按钮触发事件
//                Utils.IntentPost(null);
                break;
        }
    }
}
