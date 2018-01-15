package air1715.pillcare.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

public class PopisPregleda_Activity extends AppCompatActivity {

    Korisnik loggedUser;
    List<Pregled> todayNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_pregleda);

        loggedUser = PrijavaActivity.getLoggedUser();

        Button openNoviPregledActivity = (Button) findViewById(R.id.btnDodajNoviPregled);
        openNoviPregledActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openActivity = new Intent(PopisPregleda_Activity.this, NoviPregled_Activity.class);
                startActivity(openActivity);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FillWithData();
        NotificationBuilder();
    }

    private void FillWithData(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = DataLoadController.GetInstance(manager);
        List<Pregled> appointments = (List<Pregled>) dataControl.GetData("appointments", null);

//POCETAK
        String dateTimeUpozorenje="";
        todayNotifications = new ArrayList<Pregled>();

        Calendar c = Calendar.getInstance();
        String datum =  c.get(Calendar.DAY_OF_MONTH)+ "-" + c.get(Calendar.MONTH)+1 + "-" +c.get(Calendar.YEAR);
        Date datumD=null,datumU=null;

        for (Pregled pregled : appointments) {
            dateTimeUpozorenje=pregled.getVrijemeUpozorenja();

            //String[] datumVrijeme = dateTimeUpozorenje.split(" ");
            //String datumUpozorenja = datumVrijeme[0];

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                datumD=sdf.parse(datum);
                datumU=sdf.parse(dateTimeUpozorenje);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (pregled.isAktivan() && datumD.equals(datumU)){
                todayNotifications.add(pregled);
            }
        }

        //KRAJ

        ListView listViewAppointments=(ListView) findViewById(R.id.listViewPregledi);

        if (appointments!=null) {
            ArrayAdapter<Pregled> adapter = new ArrayAdapter<Pregled>(this, android.R.layout.simple_list_item_1, appointments);
            listViewAppointments.setAdapter(adapter);
        }
        else{
            Toast toast = Toast.makeText(this, "Niste unijeli nijedan pregled!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private boolean isNotificationVisible(int myID) {
        Intent notificationIntent = new Intent(this, PopisPregleda_Activity.class);
        PendingIntent test = PendingIntent.getActivity(this, myID, notificationIntent, PendingIntent.FLAG_NO_CREATE);

        if(test != null)
            return true;
        else
            return false;
    }

    private void NotificationBuilder(){

        for (Pregled pregled:todayNotifications) {
            if(isNotificationVisible(pregled.getId()) == false) {
                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.notification)
                                .setContentTitle("PillCare obavijest o pregledu!")
                                .setContentText(pregled.getBiljeska());

                Intent resultIntent = new Intent(this, PopisPregleda_Activity.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(pregled.getId(), mBuilder.build());
            }
        }
    }

}
