package liuliu.hotel.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class RoomModel {
    String roomId;
    int key;
    List<RoomModel> list;

    public RoomModel(String roomId, int key) {
        this.roomId = roomId;
        this.key = key;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
