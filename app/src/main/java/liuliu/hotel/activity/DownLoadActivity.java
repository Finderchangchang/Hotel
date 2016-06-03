package liuliu.hotel.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.ivsign.android.IDCReader.CopyFile;
import com.ivsign.android.IDCReader.Cvr100bMYTask;
import com.ivsign.android.IDCReader.Cvr100bTask;

import net.tsz.afinal.annotation.view.CodeNote;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import liuliu.hotel.R;
import liuliu.hotel.base.BaseActivity;
import liuliu.hotel.model.BlueToothModel;
import liuliu.hotel.model.CustomerModel;
import liuliu.hotel.model.DBLGInfo;
import liuliu.hotel.model.InvokeReturn;
import liuliu.hotel.model.PersonModel;
import liuliu.hotel.model.SerialNumModel;
import liuliu.hotel.utils.Utils;
import liuliu.hotel.web.DBHelper;
import liuliu.hotel.web.JSONUtils;
import liuliu.hotel.web.SoapObjectUtils;
import liuliu.hotel.web.WebServiceUtils;
import liuliu.hotel.web.XmlUtils;
import liuliu.hotel.web.globalfunc;

/**
 * Created by Administrator on 2016/5/19.
 */
public class DownLoadActivity extends BaseActivity {
    @CodeNote(id = R.id.login_down, click = "onClick")
    Button down;
    @CodeNote(id = R.id.login_bluth, click = "onClick")
    Button bluth;
    @CodeNote(id = R.id.login_read, click = "onClick")
    Button read;
    DBHelper dbHelper;
    @CodeNote(id = R.id.login_code, click = "onClick")
    Button code;
    @CodeNote(id = R.id.login_add, click = "onClick")
    Button btnadd;
    @CodeNote(id = R.id.login_leave, click = "onClick")
    Button btnleave;
    @CodeNote(id = R.id.login_search, click = "onClick")
    Button btnSearch;
    @CodeNote(id = R.id.login_changeCodeTime, click = "onClick")
    Button codeTime;
    @CodeNote(id = R.id.login_notice, click = "onClick")
    Button notice;
    @CodeNote(id = R.id.login_request, click = "onClick")
    Button request;
    @CodeNote(id = R.id.login_dispose, click = "onClick")
    Button dispose;
    @CodeNote(id = R.id.spread_pie_chart)
    PieChart pieChart;
    @CodeNote(id = R.id.login_rznum, click = "onClick")
    Button rznum;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_down_load);
        // CopyFile.CopyWltlib(this);
        dbHelper = new DBHelper(finalDb, this);
        PieData mPiaData = getPieData(2, 100);
        showChart(pieChart, mPiaData);
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add(0, "");
        xValues.add(1, "");
        //for (int i = 0; i < count; i++) {
        //    xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        // }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = 14;
        float quarterly2 = 86;
        //float quarterly3 = 34;
        // float quarterly4 = 38;

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        //yValues.add(new Entry(quarterly3, 2));
        //yValues.add(new Entry(quarterly4, 3));

        //y轴的集合
        //PieDataSet pieDataSet = new PieDataSet(yValues, "Quarterly Revenue 2014"/*显示在比例图上*/);
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(179, 179, 179));
        colors.add(Color.rgb(32, 128, 213));
        //colors.add(Color.rgb(255, 123, 124));
        // colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleColor(Color.rgb(205, 205, 205));
        pieChart.setLogEnabled(false);
//        pieChart.setDrawHoleEnabled(false);
//        pieChart.setDrawSliceText(false);
        pieChart.setWillNotDraw(false);
        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆
        pieChart.setDescription("");
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        // pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.isUsePercentValuesEnabled();
        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转
        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);
        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);
//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("14%");  //饼状图中间的文字
        pieChart.setCenterTextSize(20);

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setEnabled(false);
        //mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        // mLegend.setXEntrySpace(7f);
        // mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_request:
//                request();
                HashMap<String, String> properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                properties.put("pcs", "130601400");
                properties.put("lastGetTime", "2016-06-02");
                WebServiceUtils.callWebService(true, "GetAllUndownloadTZTGInfo", properties, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllUndownloadTZTGInfo");
                            System.out.println("GetAllUndownloadTZTGInfo---" + result);
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");
//                                cancelRequest();
                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                    }
                });
                break;
            case R.id.login_dispose:


                break;
            case R.id.login_rznum:
                HashMap<String, String> p = new HashMap<String, String>();
                p.put("hotelId", "1306040191");
                p.put("dateBegin", "2016-05-23");
                p.put("dateEnd", "2016-05-24");
                WebServiceUtils.callWebService(true, "GetRoomRate", p, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetRoomRate");
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");
                                finalDb.deleteAll(DBLGInfo.class);
                                finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });


                break;
            case R.id.login_down:
                properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");//旅馆代码
                properties.put("BSM", "123456");//手机识别码
                properties.put("SJM", "123456");//随机码
                properties.put("SJH", "15911111111");//手机号码
                properties.put("SJPP", "三星");//手机品牌
                WebServiceUtils.callWebService(true, "GetLGInfoByLGDM", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetLGInfoByLGDM");
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");
                                finalDb.deleteAll(DBLGInfo.class);
                                finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });


                break;
            case R.id.login_search:
                properties = new HashMap<String, String>();
                properties.put("RZSJBEGIN", "2015-01-01");//入住起始时间
                properties.put("RZSJEND", "2016-05-25");//入住截止时间
                properties.put("FH", "");
                properties.put("LKZT", "15911111111");
                properties.put("LGDM", "1306010001");
                properties.put("YS", "1");
                WebServiceUtils.callWebService(true, "SearchNative", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "SearchNative");
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");
                                // finalDb.deleteAll(DBLGInfo.class);
                                // finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });

                break;
            case R.id.login_add:

                //add();//入住
                break;
            case R.id.login_leave:
                //Leave();//离店
                break;
            case R.id.login_bluth:
                //跳转到选择蓝牙设备页面
                Utils.IntentPost(BluetoothListActivity.class);
                break;
            case R.id.login_notice:
                request();
                break;
            case R.id.login_changeCodeTime:
                properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                WebServiceUtils.callWebService(true, "GetAllCodeLastChangeTime", properties, new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllCodeLastChangeTime");
                            if (invokeReturn.isSuccess()) {
                                ToastShort("下载成功");

                            } else {
                                ToastShort("下载失败");
                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });

                break;
            case R.id.login_read:
                //读取身份证
                if (Utils.ReadString("BlueToothAddress").equals("")) {
                    ToastShort("请检查蓝牙读卡设备设置！");
                    Utils.IntentPost(BluetoothListActivity.class);//跳转登录
                } else {
                    onReadCardCvr();
                }
                break;
            case R.id.login_code:
                properties = new HashMap<String, String>();
                properties.put("lgdm", "1306010001");
                properties.put("codeName", "XZQH");//XZQH,行政区划，民族MZ,证件类型ZJLX,1男2女
                WebServiceUtils.callWebService(true, "GetCodeInfoByCodeName", properties, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (null != result) {
                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetCodeInfoByCodeName");
                            System.out.println(result);
                            // JSONUtils.parse(result);
//                            InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "CodeModel");
//                            if (invokeReturn.isSuccess()) {
//                                ToastShort("下载成功");
//                                finalDb.save((DBLGInfo) invokeReturn.getData().get(0));
//                            } else {
//                                ToastShort("下载失败");
//                            }
                        } else {
                            ToastShort("下载失败");
                        }
                        System.out.println("result==" + result);
                    }
                });
                break;

        }
    }


    //离店操作
    private void Leave() {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setSerialId("1306010001201605219502");//人员序列号
        customerModel.setCheckOutTime("2016-05-21 00:00:00");//离店时间
        String xml = customerModel.getLeaveXml(getAssetsFileData("checkOutNativeParameter.xml"));
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                System.out.println(result);
                if (!result.equals("")) {
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        //添加成功
                        System.out.println("离店成功");
                        cancelRequest();
                    } else {
                        //添加失败
                        System.out.println("离店失败");
                    }
                } else {
                    System.out.println("离店失败");
                }
            }
        });

    }

    HashMap<String, String> properties;

    private void request() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");//如果建立资源，就返回true
        properties.put("qyscm", "A0A91-2384F-5FD17-225EA-CB717");//
        WebServiceUtils.callWebService(true, "RequestServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    HashMap<String, String> properties = new HashMap<String, String>();
                    properties.put("lgdm", "1306010001");
                    properties.put("pcs", "130601400");
                    properties.put("lastGetTime", "2016-06-02");
                    WebServiceUtils.callWebService(true, "GetAllUndownloadTZTGInfo", properties, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(SoapObject result) {
                            if (null != result) {
                                InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "GetAllUndownloadTZTGInfo");
                                System.out.println("GetAllUndownloadTZTGInfo---" + result);
                                if (invokeReturn.isSuccess()) {
                                    ToastShort("下载成功");
//                                cancelRequest();
                                } else {
                                    ToastShort("下载失败");
                                }
                            } else {
                                ToastShort("下载失败");
                            }
                        }
                    });
                }
            }
        });
    }

    private void cancelRequest() {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("lgdm", "1306010001");//如果建立资源，就返回true
        WebServiceUtils.callWebService(true, "DisposeServerSource", properties, new WebServiceUtils.WebServiceCallBack() {

            @Override
            public void callBack(SoapObject result) {
                if (null != result) {
                    InvokeReturn invokeReturn = SoapObjectUtils.parseSoapObject(result, "DisposeServerSource");
                    System.out.println("DisposeServerSource" + result);
                    if (invokeReturn.isSuccess()) {
                        ToastShort("下载成功");
                    } else {
                        ToastShort("下载失败");
                    }
                } else {
                    ToastShort("下载失败");
                }
            }
        });
    }

    //添加入住人员
    private void add() {
        // List<DBLGInfo> list=finalDb.findAll(DBLGInfo.class);
        //globalfunc gfunc =(globalfunc)DownLoadActivity.this.getApplication();
        //if(list.size()>0) {
        //DBLGInfo dblgInfo = list.get(0);
        CustomerModel customerModel = new CustomerModel();
        customerModel.setAddress("2");
        customerModel.setCheckOutTime("2016-05-20 00:00:00");
        customerModel.setArea("");
        customerModel.setBirthday("1992-06-03");
        customerModel.setCardId("130682199206033463");
        customerModel.setCardType("11");
        customerModel.setCheckInSign(getVersionName());
        customerModel.setCheckInTime("2016-05-20 00:00:00");
        customerModel.setName("1");
        customerModel.setHeadphoto(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        final String num = dbHelper.getSeralNum();
        customerModel.setSerialId(num);
        customerModel.setRoomId("23");
        customerModel.setSex("1");
        customerModel.setNative("142429");
        customerModel.setNation("01");
        DBLGInfo dblgInfo = new DBLGInfo();
        dblgInfo.setLGDM("1306010001");
        dblgInfo.setQYSCM("A0A91-2384F-5FD17-225EA-CB717");
        String xml = customerModel.getXml(getAssetsFileData("checkInNativeParameter.xml"), true, dblgInfo);
        WebServiceUtils.SendDataToServer(xml, "GeneralInvoke", new WebServiceUtils.WebServiceCallBackString() {
            @Override
            public void callBack(String result) {
                if (!result.equals("")) {
                    System.out.println(result);
                    InvokeReturn invokeReturn = XmlUtils.parseXml(result, "");
                    if (invokeReturn.isSuccess()) {
                        //添加成功
                        System.out.println("添加成功");
                        SerialNumModel model = finalDb.findAll(SerialNumModel.class).get(0);
                        model.setSerialNum((Integer.parseInt(model.getSerialNum()) + 1) + "");
                        finalDb.update(model);
                    } else {
                        System.out.println("添加失败");
                        //添加失败
                    }
                }
            }
        });

    }

    @Override
    public void initEvents() {

    }

    public String getAssetsFileData(String FileName) {
        String str = "";
        try {
            InputStream is = getAssets().open(FileName);
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
     * 获取当前应用的版本号：
     */
    public String getVersionName() {
        // 获取packagemanager的实例
        String Version = "[Version:num]-[Registe:Mobile]";
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return Version.replace("num", version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version.replace("num", "1.0");
    }

    //cvr
    private void onReadCardCvr() {
        if (Utils.checkBluetooth(this, 2)) {
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "正在读取身份证信息...", true, false);
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    boolean find = false;
                    progressDialog.dismiss();
                    if (bundle != null) {
                        boolean result = bundle.getBoolean("result");
                        PersonModel person = (PersonModel) bundle.getSerializable("person_info");
                        if (result) {
                            find = true;
                            // setPerson(person);
                        } else {
                            if (null != person) {
                                find = true;
                                ToastShort(person.getPersonName());
                            }
                        }
                    }
                    if (!find) {
                        ToastShort("证件读取失败！（cvr）");
                    }
                }
            };

            new Cvr100bMYTask().startCvr100bTask(this, new Cvr100bTask.Cvr100bListener() {
                @Override
                public BlueToothModel reauestBlueDevice() {
                    BlueToothModel blue = new BlueToothModel();
                    blue.setDeviceAddress(Utils.ReadString("BlueToothAddress"));
                    return blue;
                }

                @Override
                public void onResult(boolean result, PersonModel person) {
                    Message msg = handler.obtainMessage(1);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("result", result);
                    bundle.putSerializable("person_info", person);
                    msg.setData(bundle);
                    msg.sendToTarget();
                }
            });
        } else {
            ToastShort("请检查蓝牙读卡设备设置！");
        }
    }
}
