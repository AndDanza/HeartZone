package air1715.pillcare.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.Adapters.MedicationsListRepresentation;
import air1715.pillcare.Adapters.MedicationsTileRepresentation;
import air1715.pillcare.Adapters.ModularityController;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

public class PopisLijekova_Activity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button pokreniBarcodeSkener;
    private final Activity activity = this;
    private Button therapyBtn;
    private Context context;
    private ModularityController presentationController;

    List<Lijek> medications;
    List<Proizvodac> companies;

    Button getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_lijekova_);

        //proba recycler
        context = this;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = DataLoadController.GetInstance(manager);
        medications = (List<Lijek>) dataControl.GetData("medications", null, null);
        companies = (List<Proizvodac>) dataControl.GetData("pharmaCompanies", null, null);

        View recycler = findViewById(R.id.main_recycler);
        getData = (Button) findViewById(R.id.get_data);

        if(medications != null) {
            presentationController = ModularityController.GetInstance();
            presentationController.SetData(medications, companies);
            presentationController.AddModularOption(new MedicationsTileRepresentation(recycler, context));
            presentationController.AddModularOption(new MedicationsListRepresentation(recycler, context));

            presentationController.ShowModularOption();

            getData.setVisibility(View.VISIBLE);
        }
        else{
            final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
            builder.setView(R.layout.popis_lijekova_alert);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }


        final Korisnik loggedUser = PrijavaActivity.getLoggedUser();

        if (!loggedUser.exists())
            loggedUser.save();

        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Class<? extends AppCompatActivity> activityClass = null;
                switch (item.getItemId()){
                    case R.id.lijekovi : {
                        break;
                    }
                    case R.id.ljekarne : {
                        break;
                    }
                    case R.id.pregledi : {
                        Intent changeUserData=new Intent(PopisLijekova_Activity.this,PopisPregleda_Activity.class);
                        startActivity(changeUserData);
                    }
                    case R.id.dnevniRaspored : {
                        break;
                    }
                    case R.id.IzmjenaPodataka : {
                        Intent changeUserData=new Intent(PopisLijekova_Activity.this,IzmjenaPodataka_Activity.class);
                        changeUserData.putExtra("korisnik",loggedUser);
                        startActivity(changeUserData);
                    }
                }


                return true;
            }
        });


        pokreniBarcodeSkener = (Button) findViewById(R.id.button_barCodeScanner);
        //metoda koja pokreće barcode skener
        pokreniBarcodeSkener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prikazUpute = getResources().getString(R.string.scanBarcode);

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt(prikazUpute);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presentationController.ShowModularOption();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("PopisLijekova_Activity", "Skeniranje prekinuto");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("PopisLijekova_Activity", "Skenirano");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

