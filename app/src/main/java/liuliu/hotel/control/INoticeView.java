package liuliu.hotel.control;

import java.util.List;

import liuliu.hotel.model.DBTZTGInfo;

/**
 * 通知通告
 * Created by Administrator on 2016/6/1.
 */
public interface INoticeView {
    /**
     * 加载从后台读取的通知通告信息
     *
     * @param mList
     */
    void loadView(List<DBTZTGInfo> mList);
}
