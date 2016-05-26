package liuliu.hotel.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.Calendar;

import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.base.BaseApplication;
import liuliu.hotel.config.SaveKey;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Utils {
    //base64 string转换为bitmap
    public static Bitmap getBitmapByte(String str) {
//        Bitmap bitmap;
//        byte[] s = Base64.encode(str.getBytes(), Base64.DEFAULT);
//        bitmap = BitmapFactory.decodeByteArray(s, 0, s.length);
//        return bitmap;
        try {
            byte[] buffer = Base64.decode(str.getBytes(), Base64.DEFAULT);
            if (buffer != null && buffer.length > 0) {
                return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得屏幕高度宽度
     *
     * @return Point对象 point.x宽度。point.y高度
     */
    public static Point getScannerPoint() {
        WindowManager windowManager = (WindowManager) BaseApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Point getViewPoint(View view) {
        Point point = new Point();
        view.getDisplay().getSize(point);
        return point;
    }

    public static String getAssetsFileData(Context context, String FileName) {
        String str = "";
        try {
            InputStream is = context.getAssets().open(FileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 对URL进行编码操作
     *
     * @param text
     * @return
     */
    public static String URLEncodeImage(String text) {
        if (Utils.isEmptyString(text))
            return "";

        return URLEncoder.encode(text);
    }

    /**
     * 判断字符串是否为空,为空返回空串
     * http://bbs.3gstdy.com
     *
     * @param text
     * @return
     */
    public static String URLEncode(String text) {
        if (isEmptyString(text))
            return "";
        if (text.equals("null"))
            return "";
        return text;
        /*
         * if(Utils.isEmptyString(text)) return "";
		 *
		 * return URLEncoder.encode(text);
		 */
    }

    /**
     * 检查蓝牙设备
     * http://bbs.3gstdy.com
     *
     * @param context,requestcode
     * @return
     */
    public static boolean checkBluetooth(Activity context, int requestCode) {
        /*
         * Intent serverIntent = new Intent(context, DeviceListActivity.class);
		 * context.startActivity(serverIntent); return true;
		 */

        boolean result = true;
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (null != ba) {
            if (!ba.isEnabled()) {
                result = false;
                Intent intent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivityForResult(intent, requestCode);// 或者ba.enable();
                // //同样的关闭WIFi为ba.disable();
            }
        }
        return result;
    }

    /**
     * 判断字符串是否为空
     * http://bbs.3gstdy.com
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 将图片bitmap转换为base64字符串
     * http://bbs.3gstdy.com
     *
     * @param bitmap
     * @return 根据url读取出的图片的Bitmap信息
     */
    public static String encodeBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    .trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomChar(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

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
     * 获取系统的当前日期，格式为YYYYMMDD
     * */
    public static String getSystemNowDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        String monthStr = String.valueOf(monthOfYear);
        if (monthStr.length() < 2) {
            monthStr = "0" + monthStr;
        }
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(dayOfMonth);
        if (dayStr.length() < 2) {
            dayStr = "0" + dayStr;
        }
        return String.valueOf(year) + monthStr + dayStr;
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
    public static String getImei() {
        return ((TelephonyManager) BaseApplication.getContext()
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获得当前手机的手机号码
     *
     * @return
     */
    public static String getPhoneNum() {
        TelephonyManager phoneMgr = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return phoneMgr.getLine1Number();
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 跳页传参的接口
     */
    public interface putListener {
        void put(Intent intent);
    }
}
