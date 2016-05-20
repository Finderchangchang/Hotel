package liuliu.hotel.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.utils.Utils;

/**
 * Created by Administrator on 2016/5/20.
 */
public class JSONUtils {

    //解析数据json
    public static InvokeReturn parse(String json, boolean isArray, String modelName) {
        System.out.println("json:::" + json);
        InvokeReturn mInvokeReturn = null;
        if (null != json) {
            try {
                mInvokeReturn = new InvokeReturn();
                mInvokeReturn.setSuccess(true);
                Field[] fields = new Field[0];//返回模型字段
                Object objectModel = null;//返回模型
                JSONObject jsonObject = new JSONObject(json);
                List<Object> objectList = new ArrayList<Object>();
                if (!getJsonString(jsonObject, "Success").equals("true")) {
                    mInvokeReturn.setSuccess(false);
                    String mes = jsonObject.getString("Message");
                    mInvokeReturn.setMessage(mes);
                    return mInvokeReturn;
                }
                String state = jsonObject.getString("State");
                if (!(state.equals("null") || state.equals("false") || state.equals("true"))) {
                    //是否为集合
                    if (isArray) {
                        JSONArray array = jsonObject.getJSONArray("State");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            objectModel = getObject(object,modelName);
                            objectList.add(objectModel);
                        }

                    } else {
                        //解析model
                        JSONObject stateobject = jsonObject.getJSONObject("State");
                        if (state != null) {
                            objectModel = getObject(stateobject,modelName);
                            objectList.add(objectModel);
                        }


                        mInvokeReturn.setData(objectList);
                    }
                }

            } catch (JSONException e) {

                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mInvokeReturn;
    }

    private static Object getObject(JSONObject jsonObject,String modelName) throws Exception {
        Field[] fields;
        Object objectModel = new Object();
        if (jsonObject != null) {
            if (!modelName.equals("")) {
                objectModel = getBean(modelName);
                fields = objectModel.getClass().getDeclaredFields();
                for (Field field : fields) {

                    String filedtype = field.getType().getSimpleName();

                    if (!getJsonString(jsonObject, field.getName()).equals("")) {
                        setProperty(objectModel, field.getName(), getJsonString(jsonObject, field.getName()));
                    }

                }
            }
        }
        return objectModel;
    }
    //创建的model对象，字段名，字段值
    public static void setProperty(Object bean, String propertyName, Object propertyValue) throws Exception {
        Class cls = bean.getClass();
        Method[] methos = cls.getMethods();//得到所有的方法
        for (Method m : methos) {
            if (m.getName().equalsIgnoreCase("set" + propertyName)) {
                //找到方法就注入
                m.invoke(bean, propertyValue);
                break;
            }
        }
    }
    //包名+model名
    public static Object getBean(String className) throws Exception {
        Class cls = null;
        try {
            cls = Class.forName("liuliu.hotel.model." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("类错误");
        }
        Constructor[] cons = null;
        try {
            cons = cls.getConstructors();//得到所有构造器
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("构造器错误");
        }
        //如果上面没错，就有构造方法
        Constructor defcon = cons[0];//得到默认构造器，第0个事默认构造器，无参构造方法
        Object obj = defcon.newInstance();//实例化，得到一个对象
        return obj;
    }
    /**
     * 从jsonobject中读取名为Nmae的值
     * http://bbs.3gstdy.com
     *
     * @param object,name
     * @return 根据url读取出的图片的Bitmap信息
     */
    public static String getJsonString(JSONObject object, String name) {
        try {
            return object.getString(name);
        } catch (JSONException e) {
            return "";
        }
    }
}
