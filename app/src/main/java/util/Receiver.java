package util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jarvis on 2017/3/15.
 */

@SuppressWarnings({"ALL", "DefaultFileTemplate"})
public class Receiver extends BroadcastReceiver {
    public Receiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}
