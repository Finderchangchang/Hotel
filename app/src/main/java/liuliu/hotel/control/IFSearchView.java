package liuliu.hotel.control;

import java.util.List;

import liuliu.hotel.model.CustomerModel;

/**
 * Created by Administrator on 2016/6/1.
 */
public interface IFSearchView {
    /**
     * 接收查询出的人员信息
     *
     * @param mList       人员集合
     * @param haveLoading 还有数据可以加载
     */
    void loadPerson(List<CustomerModel> mList, String haveLoading);

    /**
     * 执行离店操作的处理结果
     *
     * @param result       true,离店成功。false,离店失败
     * @param checkOutTime 离店时间
     */
    void LeaveHotel(boolean result, String checkOutTime);
}
