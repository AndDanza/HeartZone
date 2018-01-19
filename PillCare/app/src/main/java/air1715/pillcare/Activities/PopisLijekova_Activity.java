package air1715.pillcare.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.Adapters.MedicationsListRepresentation;
import air1715.pillcare.Adapters.MedicationsTileRepresentation;
import air1715.pillcare.Adapters.ModularityController;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;
import air1715.pillcare.Utils.PopUpUtils;

import static air1715.pillcare.Utils.PopUpUtils.sendMessage;

public class PopisLijekova_Activity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private Button pokreniBarcodeSkener;
    private final Activity activity = this;
    private Context context;
    private ModularityController presentationController = null;
    DataLoadController dataControl;

    List<Lijek> medications;
    List<Proizvodac> companies;

    Button switchModularRepresentaion;

    //parametri za lokaciju - google maps
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_lijekova_);

        context = this;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        dataControl = DataLoadController.GetInstance(manager);
        medications = (List<Lijek>) dataControl.GetData("medications", null);
        companies = (List<Proizvodac>) dataControl.GetData("pharmaCompanies", null);

        View recycler = findViewById(R.id.main_recycler);
        switchModularRepresentaion = (Button) findViewById(R.id.get_data);

        if (medications != null) {
            if (presentationController == null)
                SetModularRepresentation(recycler);
            else
                presentationController.SetData(medications, companies);

            presentationController.ShowModularOption();
        } else
            ShowWarning();

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

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Class<? extends AppCompatActivity> activityClass = null;
                switch (item.getItemId()) {
                    case R.id.lijekovi: {
                        break;
                    }
                    case R.id.ljekarne: {
                        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        boolean GpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        if (GpsStatus == true) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                                if (canAccessLocation()) {
                                    Intent drugstoreMap = new Intent(PopisLijekova_Activity.this, DrugstoreMap_Activity.class);
                                    startActivity(drugstoreMap);
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                                        requestPermissions(INITIAL_PERMS, LOCATION_REQUEST);
                                    }
                                }
                            } else {
                                Intent drugstoreMap = new Intent(PopisLijekova_Activity.this, DrugstoreMap_Activity.class);
                                startActivity(drugstoreMap);
                            }
                        } else {
                            String message = getResources().getString(R.string.gps_info);
                            sendMessage(PopisLijekova_Activity.this, message);
                        }
                        break;
                    }
                    case R.id.pregledi: {
                        Intent changeUserData = new Intent(PopisLijekova_Activity.this, PopisPregleda_Activity.class);
                        startActivity(changeUserData);
                        break;
                    }
                    case R.id.dnevniRaspored: {
                        break;
                    }
                    case R.id.IzmjenaPodataka: {
                        Intent changeUserData = new Intent(PopisLijekova_Activity.this, IzmjenaPodataka_Activity.class);
                        startActivity(changeUserData);
                        break;
                    }
                }


                return true;
            }
        });


        //pokretanje opcije barcode skenera
        pokreniBarcodeSkener = (Button) findViewById(R.id.button_barCodeScanner);
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


        /*
        * Tipka na formi koja je prethodno pronađena, deklarirana i inicijalizirana.
        * Pokreće pormjenu iz liste u pločice i obratno
        * */
        switchModularRepresentaion = (Button) findViewById(R.id.get_data);
        switchModularRepresentaion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentationController.ShowModularOption();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshModularRepresentation();
    }

    /*
    * Metoda koja otvara dialog u slučau da korisnik još nije unio terapiju, a nalazi se u popisu lijekova
    * */
    private void ShowWarning() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    //rezultat koji vraća barcode skener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("PopisLijekova_Activity", "Skenirano");
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                Lijek showMedication = null;

                if (medications != null) {
                    showMedication = GetMatchingMedication(result.getContents());
                    medications.clear();
                    medications.add(showMedication);
                    presentationController.SetData(medications, companies);

                    switchModularRepresentaion.setVisibility(View.VISIBLE);
                } else {
                    showMedication = (Lijek) dataControl.GetData("specificMed", result.getContents());
                    medications = new ArrayList<Lijek>();
                    medications.add(showMedication);

                    if (presentationController == null) {
                        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        dataControl = DataLoadController.GetInstance(manager);

                        View recycler = findViewById(R.id.main_recycler);
                        SetModularRepresentation(recycler);
                    } else
                        presentationController.SetData(medications, companies);

                    presentationController.ShowModularOption();

                    switchModularRepresentaion.setVisibility(View.VISIBLE);
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }

        Button refresh = (Button) findViewById(R.id.refreshRepresentation);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshModularRepresentation();

            }
        });
    }

    private void refreshModularRepresentation() {
        presentationController.ClearData();

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        dataControl = DataLoadController.GetInstance(manager);
        medications = (List<Lijek>) dataControl.GetData("medications", null);

        if (medications != null) {

            View recycler = findViewById(R.id.main_recycler);
            switchModularRepresentaion = (Button) findViewById(R.id.get_data);

            if (presentationController == null)
                SetModularRepresentation(recycler);
            else
                presentationController.SetData(medications, companies);

            presentationController.ShowModularOption();
        } else
            switchModularRepresentaion.setVisibility(View.INVISIBLE);

        PopUpUtils.sendMessage(context, getString(R.string.medications_refresh_data));

        if(medications == null)
            ShowWarning();
    }

    private void SetModularRepresentation(View recycler) {
        if (medications != null) {
            presentationController = ModularityController.GetInstance();
            presentationController.SetData(medications, companies);
            presentationController.AddModularOption(new MedicationsTileRepresentation(recycler, context));
            presentationController.AddModularOption(new MedicationsListRepresentation(recycler, context));

            switchModularRepresentaion.setVisibility(View.VISIBLE);
        }
    }

    private Lijek GetMatchingMedication(String barcode) {
        Lijek medication = null;

        for (Lijek med : medications) {
            if (med.getBarkod().equals(barcode)) {
                medication = med;
            }
        }

        return medication;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
}
