package air1715.pillcare.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import air1715.database.entiteti.DnevniRaspored;
import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;
import air1715.pillcare.Utils.AlertHandler;
import air1715.pillcare.Utils.PopUpUtils;

/**
 * The type Dnevni raspored activity.
 */
public class DnevniRaspored_Activity extends AppCompatActivity {

    /**
     * The Logged user.
     */
    Korisnik loggedUser;
    /**
     * The Therapies.
     */
    List<Terapija> therapies;
    /**
     * The Appointments.
     */
    List<Pregled> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnevni_raspored);

        loggedUser = PrijavaActivity.getLoggedUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FillWithData();


    }


    /*
     * Metoda za ucitavanje podataka sa web servisa te potom u sam view (xml)
     * Podaci se svrstavaju u dvije liste, sve terapije (therapies) te lista
     */
    private void FillWithData() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = DataLoadController.GetInstance(manager);
        therapies = (List<Terapija>) dataControl.GetData("therapies", null);
        appointments = (List<Pregled>) dataControl.GetData("daily_appointments", null);

        List<DnevniRaspored> dailySchedules = new ArrayList<>();
        if(therapies != null)
            for(Terapija therapy : therapies){
                DnevniRaspored dailySchedule = new DnevniRaspored();
                dailySchedule.setLijek(therapy.getLijek());
                dailySchedule.setPojedinacnaDoza(therapy.getPojedinacnaDoza());
                dailySchedules.add(dailySchedule);
            }
        if(appointments != null)
            for(Pregled appointment : appointments){
                DnevniRaspored dailySchedule = new DnevniRaspored();
                dailySchedule.setBiljeska(appointment.getBiljeska());
                dailySchedule.setTermin(appointment.getTermin());
                dailySchedules.add(dailySchedule);
            }

        ListView listViewAppointments = (ListView) findViewById(R.id.listViewDnevniraspored);

        if (!dailySchedules.isEmpty()) {
            ArrayAdapter<DnevniRaspored> adapter = new ArrayAdapter<DnevniRaspored>(this, android.R.layout.simple_list_item_1, dailySchedules);
            listViewAppointments.setAdapter(adapter);
        } else {
            PopUpUtils.sendMessage(this, getString(R.string.therapies_empty));
        }
    }

}
