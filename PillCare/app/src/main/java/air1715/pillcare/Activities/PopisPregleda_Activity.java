package air1715.pillcare.Activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
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
import air1715.pillcare.Utils.AlertHandler;

public class PopisPregleda_Activity extends AppCompatActivity {

    Korisnik loggedUser;
    List<Pregled> notificationsList;
    List<Pregled> appointments=null;

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
    protected void onResume() {
        super.onResume();
        if (FillWithData()!=false){
            createAlarms();
        }

    }

    private void createAlarms() {
        for (Pregled pregled : notificationsList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = sdf.parse(pregled.getVrijemeUpozorenja());
                long when = date.getTime();

                Intent alertIntent = new Intent(getApplicationContext(), AlertHandler.class);
                alertIntent.putExtra("notificationObject", pregled);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, when,
                        PendingIntent.getBroadcast(getApplicationContext(), pregled.getId(), alertIntent, PendingIntent.FLAG_ONE_SHOT));
            } catch (ParseException e) {
                Toast toast = Toast.makeText(this, "Greška", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private boolean FillWithData() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = DataLoadController.GetInstance(manager);
        appointments = (List<Pregled>) dataControl.GetData("appointments", null);

        if (appointments==null){
            return false;
        }

        String dateTimeUpozorenje = "";
        notificationsList = new ArrayList<Pregled>();

        Calendar c = Calendar.getInstance();
        String datum = c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.YEAR);
        Date datumD = null, datumU = null;

        for (Pregled pregled : appointments) {
            dateTimeUpozorenje = pregled.getVrijemeUpozorenja();

            String[] datumVrijeme = dateTimeUpozorenje.split(" ");
            String datumUpozorenja = datumVrijeme[0];

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                datumD = sdf.parse(datum);
                datumU = sdf.parse(datumUpozorenja);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (pregled.isAktivan() && datumD.getTime()<=datumU.getTime()) {
                notificationsList.add(pregled);
            }
        }

        ListView listViewAppointments = (ListView) findViewById(R.id.listViewPregledi);

        if (appointments != null) {
            ArrayAdapter<Pregled> adapter = new ArrayAdapter<Pregled>(this, android.R.layout.simple_list_item_1, appointments);
            listViewAppointments.setAdapter(adapter);
        } else {
            Toast toast = Toast.makeText(this, "Niste unijeli nijedan pregled!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return true;
    }

}
