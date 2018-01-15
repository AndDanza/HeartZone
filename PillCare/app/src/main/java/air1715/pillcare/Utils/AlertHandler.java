package air1715.pillcare.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import air1715.database.entiteti.Pregled;
import air1715.pillcare.R;

/**
 * Created by Domagoj on 15.1.2018..
 */

public class AlertHandler extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Pregled appointment = (Pregled) intent.getExtras().getSerializable("appointment");

        NotificationBuilder(appointment, context);
    }

    private void NotificationBuilder(Pregled pregled, Context context) {
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.notification)
                            .setContentTitle("PillCare obavijest o pregledu!")
                            .setContentText(pregled.getBiljeska());

            mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(pregled.getId(), mBuilder.build());

    }
}
