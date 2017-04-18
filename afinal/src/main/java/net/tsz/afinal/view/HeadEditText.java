package net.tsz.afinal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.tsz.afinal.R;

/**
 * 有头的文本输入框
 * Created by Administrator on 2017/4/18.
 */

public class HeadEditText extends LinearLayout {
    String str_left_tv;
    String str_right_tv;
    boolean have_et;//true右侧为EditText，false右侧为TextView
    float left_tv_size;
    int left_tv_color;
    int right_tv_color;
    float right_tv_size;
    String str_hint_et;
    String str_text_et;
    EditText right_et;
    TextView left_tv;
    TextView right_tv;
    boolean pwd_et;//true密码输入框

    public HeadEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeadEditText, defStyleAttr, 0);
        str_left_tv = a.getString(R.styleable.HeadEditText_head_left_tv);
        str_right_tv = a.getString(R.styleable.HeadEditText_head_right_tv);
        have_et = a.getBoolean(R.styleable.HeadEditText_head_have_et, true);
        left_tv_size = a.getDimension(R.styleable.HeadEditText_head_left_tv_size, 0);
        left_tv_color = a.getColor(R.styleable.HeadEditText_head_left_tv_color, 0);
        right_tv_color = a.getColor(R.styleable.HeadEditText_head_right_tv_color, 0);
        right_tv_size = a.getDimension(R.styleable.HeadEditText_head_right_tv_size, 0);
        str_hint_et = a.getString(R.styleable.HeadEditText_head_hint_et);
        str_text_et = a.getString(R.styleable.HeadEditText_head_text_et);
        pwd_et = a.getBoolean(R.styleable.HeadEditText_head_pwd_et, false);
        a.recycle();
        init(context);
    }

    void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.v_head_edit_text, this);
        right_et = (EditText) findViewById(R.id.right_et);
        left_tv = (TextView) findViewById(R.id.left_tv);
        right_tv = (TextView) findViewById(R.id.right_tv);
        right_tv.setVisibility(have_et ? GONE : VISIBLE);
        right_et.setVisibility(have_et ? VISIBLE : GONE);
        left_tv.setText(str_left_tv);
        if (have_et) {
            right_et.setFocusable(true);
            if (!TextUtils.isEmpty(str_hint_et)) {
                right_et.setHint(str_hint_et);
            }
            if (!TextUtils.isEmpty(str_right_tv)) {
                right_et.setText(str_right_tv);
            }
            if (right_tv_size != 0) {
                right_et.setTextSize(right_tv_size);
            }
            if (right_tv_color != 0) {
                right_et.setTextColor(right_tv_color);
            }
//            if (pwd_et) {
//                right_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            }
        } else {
            right_tv.setText(str_right_tv);
            if (right_tv_size != 0) {
                right_tv.setTextSize(right_tv_size);
            }
            if (right_tv_color != 0) {
                right_tv.setTextColor(right_tv_color);
            }
        }
        if (left_tv_size != 0) {
            left_tv.setTextSize(left_tv_size);
        }
    }

    public HeadEditText(Context context) {
        this(context, null);
    }

    public HeadEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
