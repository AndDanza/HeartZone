package air1715.pillcare.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;
import air1715.pillcare.Utils.AlertHandler;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

/**
 * Created by Marijan Hranj on 05/12/2017.
 */

public class TerapijaActivity extends AppCompatActivity {

    Context context;
    ConnectivityManager manager;
    DataLoadController dataControl;
    Terapija therapy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapija);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

        context = getApplicationContext();

        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        dataControl = DataLoadController.GetInstance(manager);

        final Lijek medication = (Lijek) getIntent().getSerializableExtra("medication");
        final Proizvodac company = (Proizvodac) getIntent().getSerializableExtra("company");

        final Korisnik loggedUser = PrijavaActivity.getLoggedUser();

        List<Object> params = new ArrayList<Object>();
        params.add(0, (Object) loggedUser);
        params.add(1, (Object) medication);


        therapy = (Terapija) dataControl.GetData("specificTherapy", (Object) params);
        Log.d("terapija", String.valueOf(therapy.getPocetak().length()));
        Log.d("terapija", therapy.getPocetak().toString());
        Log.d("terapija", String.valueOf(therapy.getPocetak().getClass()));

        LoadDataInXML(medication, company);

        ImageButton newTherapyBtn = (ImageButton) findViewById(R.id.newTherapyBtn);
        ImageButton startTherapyBtn = (ImageButton) findViewById(R.id.startTherapyBtn);
        ImageButton stopTherapyBtn = (ImageButton) findViewById(R.id.stopTherapyBtn);

        newTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (therapy == null) {
                    Intent intent = new Intent(getBaseContext(), NovaTerapijaActivity.class);
                    intent.putExtra("medication", medication);
                    intent.putExtra("company", company);
                    startActivity(intent);
                } else {
                    PopUpUtils.sendMessage(context, "Već imate dodanu terapiju s navedenim lijekom");
                }
            }
        });

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        startTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (therapy == null)
                    PopUpUtils.sendMessage(context, "Niste napravili terapiju s ovim lijekom");
                else {
                    if (therapy.getPocetak() == "null") {
                        Calendar cal = Calendar.getInstance();
                        String todayDate = format.format(cal.getTime());

                        Map params = new HashMap<String, Object>();
                        params.put("start", "1");
                        params.put("date", todayDate);
                        params.put("therapy_id", therapy.getId());
                        if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                            PopUpUtils.sendMessage(context, "Terapija je pokrenuta");
                            therapy.setPocetak(todayDate);
                            startTherapyAlarms(therapy, medication, context);
                        } else
                            PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");
                    } else {
                        PopUpUtils.sendMessage(context, "Terapija je već pokrenuta");
                    }
                }


            }
        });

        stopTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (therapy == null)
                    PopUpUtils.sendMessage(context, "Niste napravili terapiju s ovim lijekom");
                else {
                    if (therapy.getPocetak() != "null") {
                        Calendar cal = Calendar.getInstance();
                        String todayDate = format.format(cal.getTime());

                        Map params = new HashMap<String, String>();
                        params.put("start", "0");
                        params.put("date", todayDate);
                        params.put("therapy_id", therapy.getId());
                        if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                            PopUpUtils.sendMessage(context, "Terapija je zaustavljena");
                            stopTherapyAlarms(therapy, medication, context);
                        } else
                            PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");
                    } else {
                        PopUpUtils.sendMessage(context, "Terapija još nije pokrenuta");
                    }
                }
            }
        });


    }

    /*
    * Zaustavljanje alarma za terapiju ako korisnik odabere opciju "zaustavi terpaiju"
    * ulazni pramateri su terpaija za koju se gasi alarm te lijek pomću kojeg je kreiran ključ u shared
    * preferences datoteci i kontekst za dohvaćanje iste
    * */
    private void stopTherapyAlarms(Terapija therapy, Lijek medication, Context context) {
        int numberOfTherapies = therapy.getBrojDnevnihDoza();

        SharedPreferences sharedPref = context.getSharedPreferences("created_alarms", Context.MODE_PRIVATE);

        String storedAs = "";

        for (int i = 0; i < numberOfTherapies; i++) {
            storedAs = medication.getNaziv()+"_"+String.valueOf(i);

            removeSharedPreference(sharedPref, storedAs);

            Intent intent = new Intent(this, AlertHandler.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), therapy.getId(), intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    /*
     * Uklonjeni alarm i njegove pripadajuće vrijednosti u share preferences datoteci (id)
     * potrebno je ukloniti kako bi se opet mogao koristiti
     */
    private void removeSharedPreference(SharedPreferences sharedPref, String key){
        String stringValue = String.valueOf(sharedPref.getInt(key, 0));
        Log.d("shared pref", stringValue);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

    /*
    * Kreiranje intenta koji se poziva na "okidanje alarma"
    * Kreira notifikaciju za prikaz za danu terapiju i lijek u terapiji
    * */
    private Intent createAlertIntent(Terapija therapy, Lijek medication){
        Intent alertIntent = new Intent(getApplicationContext(), AlertHandler.class);
        alertIntent.putExtra("notificationObject", therapy);
        alertIntent.putExtra("medication", medication);

        return alertIntent;
    }


    private int generateAlarmID(){
        Random r = new Random();
        return r.nextInt((65000 - 100) + 1) + 100;
    }

    /*
    * Pohrana tipa ključ - vrijednost u datoteku share preferences po imenu "created_alarms".
    * Pomoću nje se dohvaćaju ID-evi alarma za danu terapiju.
    * */
    private void storeSharedPreference(Context context, int alarmID, String storingName){
        SharedPreferences sharedPref = context.getSharedPreferences("created_alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(storingName, alarmID);
        editor.commit();
    }

    /*
    * Dnevna doza terapije (tableta, dvije...) mogu se uzimati ne samo svaki dan već svaka dva, tri dana...
    * U tu svrhu potrebno je računati interval sljedeće dnevne doze u milisekundama
    * */
    private long countMilisecondsBetweenDailyDose(int daysBetweenDailyDose){
        final long dayInMiliseconds = 86400000;

        return dayInMiliseconds*daysBetweenDailyDose;
    }

    /*
    * Pokretanje alarma terapije (alarm mnagerom kreira se alarm
    * ID alarma pohranjuje se u share preference datoteku "created_alarms"
    * */
    private void startTherapyAlarms(Terapija therapy, Lijek medication, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("stored_alarms", Context.MODE_PRIVATE);
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(therapy.getPocetak());
            long when = date.getTime();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alertIntent = createAlertIntent(therapy, medication);

            long interval = calculateRepeatInterval(when, Integer.valueOf(therapy.getBrojDnevnihDoza()));
            int numberOfTherapies = therapy.getBrojDnevnihDoza();
            long daysBetweenDailyDose = countMilisecondsBetweenDailyDose(therapy.getRazmakDnevnihDoza());

            int randomID = 0;

            for (int i = 0; i < numberOfTherapies; i++) {
                randomID = generateAlarmID();
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when, daysBetweenDailyDose,
                        PendingIntent.getBroadcast(getApplicationContext(), randomID, alertIntent, 0));

                String storingName = medication.getNaziv()+"_"+String.valueOf(i);
                storeSharedPreference(context, randomID, storingName);

                when += interval;
            }

        } catch (ParseException e) {
            PopUpUtils.sendMessage(this, getString(R.string.general_error));
        }
    }

    /*
    * Od trenutka kad je korisnik pokrenuo uzimanje terpaije do kraja dana računa se vrijeme
    * u milisekundama, razlika navedenih vremena dijeli se s veličinom dnevne doze (tableta, dvije, tri...)
    * te time dobivamo razmake između doza unutar dana.
    * */
    private long calculateRepeatInterval(long startMiliseconds, int timesADay) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);

        long endOfDay = calendar.getTimeInMillis();

        long interval = (endOfDay - startMiliseconds) / timesADay;

        return interval;
    }


    private void LoadDataInXML(Lijek medication, Proizvodac company) {
        View itemView = getWindow().getDecorView();

        ImageView packImage = (ImageView) findViewById(R.id.medication_image_th);
        TextView medicationName = (TextView) findViewById(R.id.medication_name_th);
        TextView medicationStrenght = (TextView) findViewById(R.id.medication_strength_th);
        TextView medicationPillNumber = (TextView) findViewById(R.id.medication_num_pills);
        TextView medicationHelp = (TextView) findViewById(R.id.medication_help);

        TextView companyName = (TextView) findViewById(R.id.pharma_company);
        ImageView companyImage = (ImageView) findViewById(R.id.pharma_company_logo);

        Picasso.with(itemView.getContext()).load(medication.getPakiranje()).into(packImage);
        medicationName.setText(medication.getNaziv());
        medicationPillNumber.setText(String.valueOf(medication.getBrojTableta()));
        medicationStrenght.setText(String.valueOf(medication.getJacina()));
        medicationHelp.setText(medication.getUpute());

        Picasso.with(itemView.getContext()).load(company.getSlika()).into(companyImage);
        companyName.setText(company.getNaziv());

    }

}
