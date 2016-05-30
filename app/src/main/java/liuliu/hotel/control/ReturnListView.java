package liuliu.hotel.control;

import java.util.List;
import java.util.Objects;

import liuliu.hotel.model.CustomerModel;

/**
 * Created by Administrator on 2016/5/30.
 */
public interface ReturnListView {
    void SearchCustomer(boolean isTrue, List<CustomerModel>list);
}
