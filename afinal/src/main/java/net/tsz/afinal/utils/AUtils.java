package net.tsz.afinal.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/24.
 */
public class AUtils {
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
     * 获得所画的图
     *
     * @param context  系统变量
     * @param count    分成几部分
     * @param allcount 总人数
     * @return
     */
    private static PieData getPieData(Context context, int count, int allcount) {
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
        float quarterly1 = count;
        float quarterly2 = allcount - count;
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
        colors.add(Color.rgb(32, 128, 213));
        colors.add(Color.rgb(179, 179, 179));
        //colors.add(Color.rgb(255, 123, 124));
        // colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    /**
     * 显示所画的图
     *
     * @param context  系统变量
     * @param count    分成的份数
     * @param range    组件大小
     * @param pieChart 页面上显示的组件
     * @param allcount 全部房间数
     * @param hcount   在住房间数
     */
    public static void showChart(Context context, int count, float range, PieChart pieChart, int allcount, int hcount) {
        int baifenbi = 0;
        if (allcount != 0) {
            float num = hcount / allcount;
            baifenbi = Integer.parseInt(new java.text.DecimalFormat("0").format(num));
        }


        PieData pieData = getPieData(context, hcount, allcount);
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

        if (allcount != 0) {
            pieChart.setCenterText("在住率" + baifenbi * 100 + "%");  //饼状图中间的文字
        } else {
            pieChart.setCenterText("在住率" + "0%");  //饼状图中间的文字
        }
        pieChart.setCenterTextSize(12);

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
}
