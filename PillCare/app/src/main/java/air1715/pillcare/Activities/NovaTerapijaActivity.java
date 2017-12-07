package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.R;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

/**
 * Created by Marijan Hranj on 05/12/2017.
 */

public class NovaTerapijaActivity extends AppCompatActivity {

    Context context;

    Lijek medication;
    Korisnik loggedUser;

    EditText singleDoseEditText;
    EditText dailyDoseEditText;
    EditText numberOfDaysBeetwenDoseEditText;
    EditText pillsLeftWarningEditText;
    EditText therapyStartDate;
    EditText therapyEndDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_therapy_activity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

        context = getApplicationContext();

        medication = (Lijek) getIntent().getSerializableExtra("medication");
        loggedUser = PrijavaActivity.getLoggedUser();

        singleDoseEditText = (EditText) findViewById(R.id.singleDoseEditText);
        dailyDoseEditText = (EditText) findViewById(R.id.dailyDoseEditText);
        numberOfDaysBeetwenDoseEditText = (EditText) findViewById(R.id.numberOfDaysBeetwenDoseEditText);
        pillsLeftWarningEditText = (EditText) findViewById(R.id.pillsLeftWarningEditText);
        therapyStartDate = (EditText) findViewById(R.id.therapyStartDate);
        therapyEndDate = (EditText) findViewById(R.id.therapyEndDate);

        ImageView acceptTherapyImageView = (ImageView) findViewById(R.id.acceptTherapyImageView);
        ImageView cancelTherapyImageView = (ImageView) findViewById(R.id.cancelTherapyImageView);



        acceptTherapyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singleDoseEditText.getText().toString().isEmpty() || dailyDoseEditText.getText().toString().isEmpty() || numberOfDaysBeetwenDoseEditText.getText().toString().isEmpty() || pillsLeftWarningEditText.getText().toString().isEmpty() || therapyStartDate.getText().toString().isEmpty())
                    PopUpUtils.sendMessage(context, "Morate popuniti sva polja osim 'Pocetak'");
                else{
                    Terapija therapy = new Terapija();
                    therapy.setKorisnik(loggedUser);
                    therapy.setKorisnikId(loggedUser.getId());
                    therapy.setLijek(medication);
                    therapy.setLijekoviId(medication.getId());
                    therapy.setAktivna(true);
                    therapy.setBrojDnevnihDoza(Integer.parseInt(dailyDoseEditText.getText().toString()));
                    therapy.setPojedinacnaDoza(Double.parseDouble(singleDoseEditText.getText().toString()));
                    therapy.setUpozorenje(Integer.parseInt(pillsLeftWarningEditText.getText().toString()));
                    therapy.setRazmakDnevnihDoza(Integer.parseInt(numberOfDaysBeetwenDoseEditText.getText().toString()));
                    therapy.setPocetak(therapyStartDate.getText().toString());
                    therapy.setKraj(therapyEndDate.getText().toString());

                    Map params = new HashMap<String, Object>();
                    params.put("therapy", therapy);
                    if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/dodajTerapiju.php") != null) {
                        PopUpUtils.sendMessage(context, "Uspješno ste se dodali terapiju");
                        therapy.save();
                        Intent intent = new Intent(getBaseContext(), PopisLijekova_Activity.class);
                        startActivity(intent);
                    } else
                        PopUpUtils.sendMessage(context, "Problem prilikom spremanja terapije u bazu");
                }
            }
        });

        cancelTherapyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), PopisLijekova_Activity.class);
                startActivity(intent);
            }
        });

    }
}
