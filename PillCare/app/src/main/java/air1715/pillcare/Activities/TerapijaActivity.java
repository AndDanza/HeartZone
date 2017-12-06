package air1715.pillcare.Activities;

import android.content.Intent;
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

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.R;

/**
 * Created by Marijan Hranj on 05/12/2017.
 */

public class TerapijaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapija);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

        final Lijek medication = (Lijek) getIntent().getSerializableExtra("medication");
        final Proizvodac company = (Proizvodac) getIntent().getSerializableExtra("company");

        LoadDataInXML(medication, company);

        //TODO
        //potrebno dovesti objekt korisnika
        //final Korisnik loggedUser = (Korisnik) getIntent().getSerializableExtra("korisnik");

        ImageButton newTherapyBtn = (ImageButton) findViewById(R.id.newTherapyBtn);
        ImageButton startTherapyBtn = (ImageButton) findViewById(R.id.startTherapyBtn);
        ImageButton stopTherapyBtn = (ImageButton) findViewById(R.id.stopTherapyBtn);

        newTherapyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NovaTerapijaActivity.class);
                //intent.putExtra("korisnik", loggedUser);
                startActivity(intent);
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
