package air1715.pillcare.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import javax.xml.transform.TransformerFactoryConfigurationError;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.R;

/**
 * Created by Domagoj on 15.1.2018..
 */

public class AlertHandler extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object extra = intent.getExtras().getSerializable("notificationObject");

        /*
        *zaprimljen objekt tipa klase pregled poziva drugačiju notifikaciju (drugačiji tekst i ikona) od
        *one za terapiju
        * */
        if(extra.getClass() == Pregled.class){
            Pregled appointment = (Pregled) extra;
            NotificationBuilder(appointment.getBiljeska(),
                    appointment.getId(),
                    context,
                    R.string.appointment_notification,
                    R.mipmap.notification);
        }
        else if(extra.getClass() == Terapija.class){
            Terapija therapy = (Terapija) extra;
            Lijek medication = (Lijek) intent.getExtras().getSerializable("medication");
            NotificationBuilder(medication.getNaziv(),
                    therapy.getId(),
                    context,
                    R.string.pill_notification,
                    R.mipmap.ic_pill_notification);
        }
    }


    private void NotificationBuilder(String contentString, int notificationID, Context context, int titleStringID, int iconID) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(iconID)
                        .setContentTitle(context.getString(titleStringID))
                        .setContentText(contentString);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationID, mBuilder.build());

    }
}
