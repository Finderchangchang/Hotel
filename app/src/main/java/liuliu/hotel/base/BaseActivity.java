package liuliu.hotel.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalDb;

import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
import liuliu.hotel.R;
import liuliu.hotel.ui.activity.MainActivity;

/**
 * BaseActivity声明相关通用方法
 * <p/>
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    public FinalDb finalDb;
    TextView title_name_tv;
    public Toolbar toolbar;
    public ImageLoader mLoader;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finalDb = FinalDb.create(this, false);
        mLoader = ImageLoaderFactory.create(this).tryToAttachToContainer(this);
        initViews();
        initEvents();
    }

    /**
     * 设置顶部toolbar样式以及文字效果
     *
     * @param val 需要显示的Title的内容
     */
    public void setTitleBar(String val) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title_name_tv = (TextView) findViewById(R.id.title_name_tv);
        title_name_tv.setText(val);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public abstract void initViews();

    public abstract void initEvents();

    public void ToastShort(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
