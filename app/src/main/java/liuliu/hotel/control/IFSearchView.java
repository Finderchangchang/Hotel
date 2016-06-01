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
     * @param mList 人员集合
     */
    void loadPerson(List<CustomerModel> mList);
}
