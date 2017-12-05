package air1715.pillcare.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

public class PopisLijekova_Activity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button pokreniBarcodeSkener;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_lijekova_);

        final Korisnik loggedUser = (Korisnik) getIntent().getSerializableExtra("korisnik");

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
                int id = item.getItemId();

                if (id == R.id.lijekovi) {
                    Toast.makeText(PopisLijekova_Activity.this, "LIJEKOVI", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.ljekarne) {
                    Toast.makeText(PopisLijekova_Activity.this, "LJEKARNE", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.pregledi) {
                    Intent intent = new Intent(getBaseContext(), PopisPregleda_Activity.class);
                    intent.putExtra("korisnik", loggedUser);
                    startActivity(intent);
                } else if (id == R.id.dnevniRaspored) {
                    Toast.makeText(PopisLijekova_Activity.this, "DNEVNI RASPORED", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.terapija){
                    Intent intent = new Intent(getBaseContext(), PopisLijekova_Activity.class);
                    intent.putExtra("korisnik", loggedUser);
                    startActivity(intent);
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

        Button getData = (Button) findViewById(R.id.get_data);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                DataLoadController dataControl = new DataLoadController(manager);
                List<Lijek> medications = (List<Lijek>) dataControl.GetData("medications", null);
                List<Proizvodac> companiesData = (List<Proizvodac>) dataControl.GetData("pharmaCompanies", null);

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

