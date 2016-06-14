package liuliu.hotel.utils;

import android.os.Looper;
import android.os.Message;

import net.tsz.afinal.FinalDb;

/**
 * Created by Administrator on 2015/11/25.
 */
public class DBHelperUtils extends Thread {
    FinalDb finalDb;
    DBHelperListener dbHelperListener;

    public DBHelperUtils(FinalDb db, DBHelperListener dbHelperListener) {
        finalDb = db;
        this.dbHelperListener = dbHelperListener;
    }

    public interface DBHelperListener {
        void dbHelper();
        void dbHelperResult();
    }

    @Override
    public void run() {
        Looper.prepare();
        dbHelperListener.dbHelper();
        handler.sendEmptyMessage(1);
        super.run();
        Looper.loop();
    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            dbHelperListener.dbHelperResult();
            super.handleMessage(msg);
        }
    };
}
