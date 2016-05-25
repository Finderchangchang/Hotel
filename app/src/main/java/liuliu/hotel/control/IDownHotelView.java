package liuliu.hotel.control;

import liuliu.hotel.model.DBLGInfo;

/**
 * 设备绑定旅馆
 * Created by Administrator on 2016/5/19.
 */
public interface IDownHotelView {

    /**
     * 验证码比对结果
     *
     * @param result true，成功。
     *               false，失败。
     */
    void checkHotel(boolean result,String mes);
}
