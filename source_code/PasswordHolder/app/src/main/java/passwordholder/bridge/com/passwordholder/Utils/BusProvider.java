package passwordholder.bridge.com.passwordholder.Utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Anu on 12/10/2015.
 */
public final class BusProvider {
    // other methods here
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    public static void postOnMain(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getInstance().post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    getInstance().post(event);

                }
            });
        }
    }
}
