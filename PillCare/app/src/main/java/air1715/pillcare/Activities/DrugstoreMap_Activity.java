package air1715.pillcare.Activities;


import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air1715.pillcare.R;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PharmacyMapClass;

import static air1715.pillcare.Utils.PopUpUtils.sendMessage;

public class DrugstoreMap_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugstore_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button loadData = (Button) findViewById(R.id.load_pharmacies);
        loadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Location loc = mMap.getMyLocation();
                if (loc != null) {
                    PharmaciesOnMap(loc.getLatitude(), loc.getLongitude());

                }
                else{
                    String message = getResources().getString(R.string.warning_location);
                    sendMessage(DrugstoreMap_Activity.this, message);
                }

            }
        });
    }

    /*
    * Kad je karta spremna lociraj korisnika te potom prikaži informacije (legendu)
    * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        ShowInformation();
    }


    /*
    * Prikaz legende korištenih ikona na karti
    * */
    private void ShowInformation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setView(R.layout.pharmacy_alert);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    /*
    * Dohvaćanje ljekarni prema korisnikovoj lokaciji.
    * Ljekarne se dohvaćaju na serveru i šalju u obliku json-a
    * */
    private JSONArray getDrugstoresForLocation(double lat, double lng) throws JSONException {
        String storesScript = "https://pillcare.000webhostapp.com/dohvatiLjekarne.php";

        Map params = new HashMap<String, String>();
        params.put("location", String.valueOf(lat)+","+String.valueOf(lng));

        JSONArray response = HttpUtils.sendGetRequestArray(params, storesScript);

        return response;
    }

    /*
    * Kontrola prikaza ljekarni na karti
    * For petljom iterira se kroz listu ljekarni (PharmacyMapClass) i iscrtavaju na karti
    * */
    private void PharmaciesOnMap(double lat, double lng) {
        JSONArray response = null;
        List<PharmacyMapClass> drugstores = null;

        try {
            response = getDrugstoresForLocation(lat, lng);

            if (response != null) {
                drugstores = parsePharmaciesJSON(response);
                drawDrugstoresOnMap(drugstores);
            }
            else{
                String message = getResources().getString(R.string.error_pharmacies);
                sendMessage(DrugstoreMap_Activity.this, message);
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionMedications. " + e.getLocalizedMessage());
        }
    }

    /*
    * JSONArray odgovor dobiven od servisa razbija se na JSONObject-e
    * Unutar klase PharmacyMapClass JSON objekti pohranjuju se u objekt tipa klase i potom u listu drugstores
    * */
    private List<PharmacyMapClass> parsePharmaciesJSON(JSONArray jsonArray) throws JSONException {
        List<PharmacyMapClass> drugstores = new ArrayList<PharmacyMapClass>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            drugstores.add(new PharmacyMapClass(jsonobject));
        }

        return drugstores;
    }

    /*
    * Prikazivanje markera za ljekarnu na karti
    * */
    private void drawDrugstoresOnMap(List<PharmacyMapClass> drugstores) {

        for (PharmacyMapClass pharmacy:drugstores) {
            LatLng drugstore = new LatLng(pharmacy.getLatitude(),pharmacy.getLongitude());
            MarkerOptions locationOptions = new MarkerOptions();
            locationOptions.title(pharmacy.getName());
            locationOptions.position(drugstore);

            //svaka boja određuje radno vrijeme ljekarne
            if(pharmacy.getOpen() == 1)
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));//još radi
            else if(pharmacy.getOpen() == 0)
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));//kraj radnog vremena
            else
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));//ne zna se

            mMap.addMarker(locationOptions);
        }
    }
}
