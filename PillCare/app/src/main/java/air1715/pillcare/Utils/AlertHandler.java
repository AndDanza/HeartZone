package air1715.pillcare.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.TransformerFactoryConfigurationError;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.R;

/**
 * The type Alert handler.
 */
public class AlertHandler extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Object extra = intent.getExtras().getSerializable("notificationObject");

        /*
        *Zaprimljen objekt tipa klase pregled poziva drugaciju notifikaciju (drugaciji tekst i ikona) od
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
                    R.mipmap.notification_lijek);

            if(therapy.getStanje() <= 5){
                NotificationBuilder("Terapije: "+medication.getNaziv(),
                    1548,
                    context,
                    R.string.therapy_pill_status,
                    R.mipmap.notification_lijek);
            }

            //ako postoji veza sa internetom
            //ako veza ne postoji prvi put kad se korisnik poveže i oglasi se notifikacija
            //racuna se stanje koje bi trebalo biti na danasnji dan i ono se ažurira
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = manager.getActiveNetworkInfo();

            if(netInfo != null && netInfo.isConnectedOrConnecting()) {
                updateTherapyPillStatus(therapy, medication);

                storeTherapyPillStatus(therapy);
            }
        }
    }

    private void storeTherapyPillStatus(Terapija therapy) {
        Map params = new HashMap<String, Object>();
        params.put("therapy", therapy);
        HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/azurirajTerapiju.php");
    }

    private Terapija updateTherapyPillStatus(Terapija therapy, Lijek medication) {
        Terapija updatedTherapyState = calculateCurrentPillStatus(therapy);

        return updatedTherapyState;
    }

    /**
     * Calculate current pill status terapija.
     *
     * @param therapy the therapy
     * @return the terapija
     */
    public Terapija calculateCurrentPillStatus(Terapija therapy) {
        double therapyState = 0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //dohvacanje pocetka terapije
            String therapyStart = therapy.getPocetak();
            String[] datumVrijeme = therapyStart.split(" ");
            String startDate = datumVrijeme[0];
            Date date = sdf.parse(startDate);

            //dohvaćanje danasnjeg datuma
            Calendar c = Calendar.getInstance();
            String todayDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DAY_OF_MONTH);
            Date todayDateParse = sdf.parse(todayDate);

            //razlika dvaju datum je broj dana koliko terapija zasad traje
            long diff = todayDateParse.getTime() - date.getTime();
            //days + 1 jer inace prvi dan ne bi uzeli u obzir prilikom izracuna
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

            //stanje terapije je broj dana trajanja (zasad) pomnozen s brojem dnevnih doza (1 , 2 dnevne doze)
            // i sve pomnozeno sa pojedinacnom dozom (tableta, dvije...)
            therapyState = days  * therapy.getPojedinacnaDoza();

            therapy.setStanje(therapy.getStanje() - (int)Math.round(therapyState));
        }
        catch (ParseException e){}


        return therapy;
    }


    private void NotificationBuilder(String contentString, int notificationID, Context context, int titleStringID, int iconID) {
        Uri soundAlert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(iconID)
                        .setContentTitle(context.getString(titleStringID))
                        .setContentText(contentString)
                        .setSound(soundAlert);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationID, mBuilder.build());

    }
}
