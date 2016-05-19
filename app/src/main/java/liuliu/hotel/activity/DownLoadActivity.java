package liuliu.hotel.activity;

import android.view.View;
import android.widget.Button;

import net.tsz.afinal.annotation.view.CodeNote;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.web.WebServiceUtils;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DownLoadActivity extends BaseActivity {
    @CodeNote(id = R.id.login_down, click = "onClick")
    Button down;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_down:
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("lgdm", "1306020001");
                properties.put("BSM", "123456");
                WebServiceUtils.callWebService(WebServiceUtils.MYURL,"GetLGInfoByLGDM",properties,new WebServiceUtils.WebServiceCallBack(){

                    @Override
                    public void callBack(SoapObject result) {
                        if(null!=result){

                        }
                        System.out.println("result=="+result);
                    }
                });


                break;
        }
    }

    @Override
    public void initEvents() {

    }
}
