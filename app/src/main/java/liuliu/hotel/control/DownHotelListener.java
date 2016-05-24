package liuliu.hotel.control;

import liuliu.hotel.model.DBLGInfo;

/**
 * 下载
 * Created by Administrator on 2016/5/19.
 */
public class DownHotelListener {
    IDownHotelView mView;

    public DownHotelListener(IDownHotelView mView) {
        this.mView = mView;
    }

    /**
     * 发送旅馆代码与随机码到后台
     *
     * @param hotelId   旅馆代码
     * @param code      随机码
     * @param imei      手机识别码
     * @param phoneNum  手机号码
     * @param phoneType 手机品牌型号
     */
    public void pushCode(String hotelId, String code, String imei, String phoneNum, String phoneType) {
        mView.checkHotel(true, DBLGInfo.getTestHotel());
    }
}
