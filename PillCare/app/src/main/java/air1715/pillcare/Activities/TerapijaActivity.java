package air1715.pillcare.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
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
        params.add(0,(Object) loggedUser);
        params.add(1, (Object) medication);


        therapy = (Terapija) dataControl.GetData("specificTherapy", (Object)params);

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
                    Calendar cal = Calendar.getInstance();
                    String todayDate = format.format(cal.getTime());

                    Map params = new HashMap<String, Object>();
                    params.put("start", "1");
                    params.put("date", todayDate);
                    params.put("therapy_id", therapy.getId());
                    if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                        PopUpUtils.sendMessage(context, "Terapija je pokrenuta");
                        startTherapyAlarms(therapy, medication);
                    }
                    else
                        PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");
                }


            }
        });

        stopTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (therapy == null)
                    PopUpUtils.sendMessage(context, "Niste napravili terapiju s ovim lijekom");
                else {
                    Calendar cal = Calendar.getInstance();
                    String todayDate = format.format(cal.getTime());

                    Map params = new HashMap<String, String>();
                    params.put("start", "0");
                    params.put("date", todayDate);
                    params.put("therapy_id", therapy.getId());
                    if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                        PopUpUtils.sendMessage(context, "Terapija je zaustavljena");
                        stopTherapyAlarms(therapy);
                    }
                    else
                        PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");
                }
            }
        });


    }

    private void stopTherapyAlarms(Terapija therapy) {
        Intent intent = new Intent(this, AlertHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), therapy.getId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void startTherapyAlarms(Terapija therapy, Lijek medication) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = sdf.parse(therapy.getPocetak());
            long when = date.getTime();

            sdf = new SimpleDateFormat("HH");
            date = sdf.parse(String.valueOf(therapy.getRazmakDnevnihDoza()));
            long every = date.getTime();

            Intent alertIntent = new Intent(getApplicationContext(), AlertHandler.class);
            alertIntent.putExtra("notificationObject", therapy);
            alertIntent.putExtra("medication", medication);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when, every,
                    PendingIntent.getBroadcast(getApplicationContext(), therapy.getId(), alertIntent, PendingIntent.FLAG_ONE_SHOT));
        } catch (ParseException e) {
            Toast toast = Toast.makeText(this, "Greška", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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
