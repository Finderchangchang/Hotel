package liuliu.hotel.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.base.BaseApplication;
import liuliu.hotel.config.SaveKey;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Utils {
    /**
     * 根据键值对读取存储在本地的数据
     *
     * @param key 键
     * @return 存储的值
     */
    public static String ReadString(String key) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SaveKey.KEY_Preferences_name,
                Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getString(key, "");
        } else {
            return "";
        }

    }

    /**
     * 带参数的跳页
     *
     * @param cla      需要跳转到的页面
     * @param listener 传参的接口
     */
    public static void IntentPost(Class cla, putListener listener) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setClass(BaseApplication.getContext(), cla);
        if (listener != null) {
            listener.put(intent);
        }
        BaseApplication.getContext().startActivity(intent);
    }

    /**
     * 不带参数的跳页
     *
     * @param cla 需要跳转到的页面
     */
    public static void IntentPost(Class cla) {
        IntentPost(cla, null);
    }

    /**
     * 将内容以键值对的形式存储在本地
     *
     * @param key   键
     * @param value 值
     */
    public static void WriteString(String key, String value) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SaveKey.KEY_Preferences_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 判断ip地址是否符合格式（10.0.3.2）
     *
     * @param ip 需要检测的ip地址
     * @return 是否符合规定，true为符合。
     */
    public static boolean checkIP(String ip) {
        if (Utils.getContainSize(ip, ".") == 3 && ip.length() >= 7) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得key在val中存在的个数
     *
     * @param val 字符串
     * @param key 包含在key中的某字符
     * @return 存在的个数
     */
    public static int getContainSize(String val, String key) {
        if (val.contains(key)) {
            int length = val.length() - val.replace(key, "").length();
            if (length > 0) {
                return length;
            }
        }
        return 0;
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     *
     * @param url
     * @return 根据url读取出的图片的Bitmap信息
     */
    public static Bitmap getBitmapByFile(String url) {
        if (url != "" && url != null) {
            try {
                FileInputStream fis = new FileInputStream(url);
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 设置TotalListView(自定义)的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerImageBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    /**
     * 截取指定字符串并添加并在后面添加...
     *
     * @param val    截取前的字符串
     * @param length 截取字符长度
     * @return 处理之后的结果
     */
    public static String cutStringToDian(String val, int length) {
        if (val.length() >= length) {
            return val.substring(0, length) + "...";
        } else {
            return val;
        }
    }

    //得到手机的imei
    public static String getImei(Context context) {
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 跳页传参的接口
     */
    public interface putListener {
        void put(Intent intent);
    }
}
