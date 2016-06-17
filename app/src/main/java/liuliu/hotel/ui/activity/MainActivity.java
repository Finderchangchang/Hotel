package liuliu.hotel.ui.activity;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.ChangeItem;
import net.tsz.afinal.model.ItemModel;
import net.tsz.afinal.view.NormalDialog;

import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.ui.frag.MainFragment;
import liuliu.hotel.ui.frag.SearchFragment;
import liuliu.hotel.ui.frag.SettingFragment;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {
    public static MainActivity mInstance;
    @CodeNote(id = R.id.main_frag)
    FrameLayout frag_layout;
    @CodeNote(id = R.id.main_ll, click = "onClick")
    LinearLayout main_ll;
    @CodeNote(id = R.id.search_ll, click = "onClick")
    LinearLayout search_ll;
    @CodeNote(id = R.id.setting_ll, click = "onClick")
    LinearLayout setting_ll;
    @CodeNote(id = R.id.main_iv)
    ImageView main_iv;
    @CodeNote(id = R.id.search_iv)
    ImageView search_iv;
    @CodeNote(id = R.id.setting_iv)
    ImageView setting_iv;
    @CodeNote(id = R.id.main_tv)
    TextView main_tv;
    @CodeNote(id = R.id.search_tv)
    TextView search_tv;
    @CodeNote(id = R.id.setting_tv)
    TextView setting_tv;
    MainFragment main_frag;
    SearchFragment search_frag;
    SettingFragment setting_frag;
    //底部菜单相关联
    List<ItemModel> mItems;
    int mClick = 0;
    List<ChangeItem> listbtn;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mItems = new ArrayList<>();
        listbtn = new ArrayList<>();
        mInstance = this;
    }

    @Override
    public void initEvents() {
        mItems.add(new ItemModel("首页", R.mipmap.main_normal, R.mipmap.main_pressed));
        mItems.add(new ItemModel("搜索", R.mipmap.search_normal, R.mipmap.search_pressed));
        mItems.add(new ItemModel("设置", R.mipmap.setting_normal, R.mipmap.setting_pressed));
        listbtn.add(new ChangeItem(main_tv, main_iv));
        listbtn.add(new ChangeItem(search_tv, search_iv));
        listbtn.add(new ChangeItem(setting_tv, setting_iv));
        setItem(0);//显示首页
    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_ll:
                setItem(0);
                break;
            case R.id.search_ll:
                setItem(1);
                break;
            case R.id.setting_ll:
                setItem(2);
                break;
        }
    }

    private void setItem(int position) {
        //恢复成未点击状态
        listbtn.get(mClick).getTv().setTextColor(getResources().getColor(R.color.normal_lab));//正常字体颜色
        listbtn.get(mClick).getIv().setImageResource(mItems.get(mClick).getNormal_img());
        //设置为点击状态
        listbtn.get(position).getTv().setTextColor(getResources().getColor(R.color.pressed_lab));//点击以后字体颜色
        listbtn.get(position).getIv().setImageResource(mItems.get(position).getPressed_img());
        mClick = position;
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (main_frag != null) {
            transaction.hide(main_frag);
        }
        if (search_frag != null) {
            transaction.hide(search_frag);
        }
        if (setting_frag != null) {
            transaction.hide(setting_frag);
        }
        switch (position) {
            case 0:
                if (main_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    main_frag = new MainFragment();
                    transaction.add(R.id.main_frag, main_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(main_frag);
                }
                break;
            case 1:
                if (search_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    search_frag = new SearchFragment();
                    transaction.add(R.id.main_frag, search_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(search_frag);
                }
                break;
            case 2:
                if (setting_frag == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    setting_frag = new SettingFragment();
                    transaction.add(R.id.main_frag, setting_frag);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(setting_frag);
                }
                break;
        }
        transaction.commit();
    }

    NormalDialog llDialog;

    /**
     * 点击返回弹出消息框，确定后完全退出系统
     *
     * @param keyCode key的编码
     * @param event   key的时间
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            llDialog.setMiddleMessage("确认要退出系统？");
            llDialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//点击确定按钮
                    llDialog.cancel();
                    MainActivity.this.finish();//完全退出系统
                }
            });
            llDialog.setOnNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llDialog.cancel();
                }
            });//点击取消按钮
            llDialog.show();
        }
        return false;
    }
}
