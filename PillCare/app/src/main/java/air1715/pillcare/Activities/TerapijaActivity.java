package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

/**
 * Created by Marijan Hranj on 05/12/2017.
 */

public class TerapijaActivity extends AppCompatActivity {

    Context context;
    ConnectivityManager manager;
    DataLoadController dataControl;

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
        dataControl = new DataLoadController(manager);

        final Lijek medication = (Lijek) getIntent().getSerializableExtra("medication");
        final Proizvodac company = (Proizvodac) getIntent().getSerializableExtra("company");

        final Korisnik loggedUser = PrijavaActivity.getLoggedUser();

        LoadDataInXML(medication, company);

        ImageButton newTherapyBtn = (ImageButton) findViewById(R.id.newTherapyBtn);
        ImageButton startTherapyBtn = (ImageButton) findViewById(R.id.startTherapyBtn);
        ImageButton stopTherapyBtn = (ImageButton) findViewById(R.id.stopTherapyBtn);

        newTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Terapija therapy = (Terapija) dataControl.GetData("specificTherapy", loggedUser, medication);
                if (therapy == null) {
                    Intent intent = new Intent(getBaseContext(), NovaTerapijaActivity.class);
                    intent.putExtra("medication", medication);
                    startActivity(intent);
                } else {
                    PopUpUtils.sendMessage(context, "Već imate dodanu navedenu terapiju");
                }
            }
        });

        startTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map params = new HashMap<String, Object>();
                params.put("start", "1");
                params.put("user", loggedUser);
                params.put("medicament", medication);
                if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                    PopUpUtils.sendMessage(context, "Terapija je pokrenuta");
                } else
                    PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");

            }
        });

        stopTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map params = new HashMap<String, Object>();
                params.put("start", "0");
                params.put("user", loggedUser);
                params.put("medicament", medication);
                if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/pokreniZaustaviTerapiju.php") != null) {
                    PopUpUtils.sendMessage(context, "Terapija je zaustavljenja");
                } else
                    PopUpUtils.sendMessage(context, "Problem prilikom spajanja na bazu");
            }
        });


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
