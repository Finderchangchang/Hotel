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
     * @param total 人员总数
     * @param stay  在住人数
     */
    void GetPersonNum(String total, int stay);

    /**
     * 加载在住人员列表
     *
     * @param list 在住人员
     */
    void LoadStayPerson(List<CustomerModel> list);

    /**
     * 执行离店操作的处理结果
     *
     * @param result true,离店成功。false,离店失败
     */
    void LeaveHotel(boolean result);
}
