package liuliu.hotel.ui.activity;

import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.CodeNote;
import net.tsz.afinal.model.CodeModel;

import java.util.List;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageReuseInfo;
import in.srain.cube.image.ImageReuseInfoManger;
import in.srain.cube.image.impl.DefaultImageLoadHandler;
import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.config.Key;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.utils.Utils;

/**
 * 人员详细信息
 * Created by Administrator on 2016/5/26.
 */
public class PersonDetailActivity extends BaseActivity {
    public static PersonDetailActivity mInstance;
    @CodeNote(id = R.id.toolbar)
    Toolbar toolbar;
    @CodeNote(id = R.id.user_img_iv)
    ImageView user_img_iv;
    @CodeNote(id = R.id.user_name_tv)
    TextView user_name_tv;
    @CodeNote(id = R.id.person_sex_tv)
    TextView person_sex_tv;
    @CodeNote(id = R.id.person_idcard_tv)
    TextView person_idcard_tv;
    @CodeNote(id = R.id.person_nation_tv)
    TextView person_nation_tv;
    @CodeNote(id = R.id.person_birthday_tv)
    TextView person_birthday_tv;
    @CodeNote(id = R.id.person_native_tv)
    TextView person_native_tv;//民族
    @CodeNote(id = R.id.persn_native_tv)
    TextView persn_native_tv;
    @CodeNote(id = R.id.detail_address_tv)
    TextView detail_address_tv;
    @CodeNote(id = R.id.card_type_tv)
    TextView card_type_tv;//证件类型
//    @CodeNote(id = R.id.default_img_iv)
//    ImageView default_img_iv;//默认图片效果
    CustomerModel model;//入住旅客信息
    List<CodeModel> list;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_person_detail);
    }

    @Override
    public void initEvents() {
        model = new CustomerModel();
        mInstance = this;

        model = (CustomerModel) getIntent().getSerializableExtra(Key.Person_Detail_Model);//获得流水号
        model.setHeadphoto(Utils.getBitmapByte(getIntent().getStringExtra("image")));
        setTitleBar("旅客详情");
        if(null!=model.getHeadphoto()){
            user_img_iv.setImageBitmap(model.getHeadphoto());
        }else{
            user_img_iv.setImageResource(R.mipmap.main_zhengjian);
            //user_img_iv.setImageBitmap(BitmapFactory.decodeResource(R.mipmap.main_zhengjian));
        }
//        if (model.getUrl() != null) {
//            user_img_iv.loadImage(mLoader, model.getUrl());
//            default_img_iv.setVisibility(View.GONE);
//            user_img_iv.setVisibility(View.VISIBLE);
//        } else {
//            default_img_iv.setVisibility(View.VISIBLE);
//            user_img_iv.setVisibility(View.GONE);
//        }
        user_name_tv.setText(model.getName());
        if (("2").equals(model.getSex())) {
            person_sex_tv.setText("女");
        }else{
            person_sex_tv.setText("男");
        }
        if (model.getBirthday().length() == 8) {
            StringBuffer buffer = new StringBuffer(model.getBirthday());
            buffer.insert(4, "年").insert(7, "月").insert(10, "日");
            person_birthday_tv.setText(buffer);
        }
        list = finalDb.findAllByWhere(CodeModel.class, "CodeName='ZJLX' AND KEY='" + model.getCardType() + "'");
        if (list != null) {
            if (list.size() > 0) {
                StringBuffer cardtype = new StringBuffer(list.get(0).getVal());
                switch (cardtype.length()) {
                    case 3:
                        cardtype.insert(1, "  ").insert(4, "  ");
                        break;
                    case 2:
                        cardtype.insert(1, "      ");
                        break;
                }
                card_type_tv.setText(cardtype.toString());
            }
        }
        list = finalDb.findAllByWhere(CodeModel.class, "CodeName='XZQH' AND KEY='" + model.getNative().substring(0, 4) + "'");
        if (list != null) {
            if (list.size() > 0) {
                persn_native_tv.setText(list.get(0).getVal());
            }
        }
        person_idcard_tv.setText(model.getCardId());//证件号码
        person_nation_tv.setText(model.getNation());
        detail_address_tv.setText(model.getAddress());
    }
}
