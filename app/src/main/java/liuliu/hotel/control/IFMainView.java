package liuliu.hotel.control;

import java.util.List;

import liuliu.hotel.model.CustomerModel;

/**
 * 主页面Fragment结果逻辑处理
 * Created by Administrator on 2016/5/30.
 */
public interface IFMainView {
    /**
     * 获得旅馆所有人员数量，以及在住人数
     *
     * @param hcount      在住房间数
     * @param allhcount   全部房间数
     * @param personcount 在住总人数
     */
    void GetPersonNum(int hcount, int allhcount, int personcount);

    /**
     * 加载在住人员列表
     *
     * @param list      在住人员
     * @param isRefresh 是否为下拉刷新
     */
    void LoadStayPerson(List<CustomerModel> list, boolean isRefresh);

    /**
     * 执行离店操作的处理结果
     *
     * @param result true,离店成功。false,离店失败
     */
    void LeaveHotel(boolean result);
}
