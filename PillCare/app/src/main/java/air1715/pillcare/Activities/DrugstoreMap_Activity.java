package air1715.pillcare.Activities;


import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
        setContentView(R.layout.activity_drugsotre_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        PharmaciesOnMap(null);
        ShowInformation();
    }

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



    private JSONArray getDrugstoresForLocation(Location current) throws JSONException {
        String storesScript = "https://pillcare.000webhostapp.com/dohvatiLjekarne.php";

        Map params = new HashMap<String, String>();
        params.put("location", "46.305322,16.328033" /*tu dolazi varijabla location*/ );

        JSONArray response = HttpUtils.sendGetRequestArray(params, storesScript);

        return response;
    }

    private void PharmaciesOnMap(Location current) {
        JSONArray response = null;
        List<PharmacyMapClass> drugstores = null;

        try {
            response = getDrugstoresForLocation(current);

            if (response != null) {
                drugstores = parsePharmaciesJSON(response);
                drawDrugstoresOnMap(drugstores);
            }
            else{
                sendMessage(this, "Nije moguće učitati ljekarne");
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionMedications. " + e.getLocalizedMessage());
        }
    }

    private List<PharmacyMapClass> parsePharmaciesJSON(JSONArray jsonArray) throws JSONException {
        List<PharmacyMapClass> drugstores = new ArrayList<PharmacyMapClass>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            drugstores.add(new PharmacyMapClass(jsonobject));
        }

        return drugstores;
    }

    private void drawDrugstoresOnMap(List<PharmacyMapClass> drugstores) {

        for (PharmacyMapClass pharmacy:drugstores) {
            LatLng drugstore = new LatLng(pharmacy.getLatitude(),pharmacy.getLongitude());
            MarkerOptions locationOptions = new MarkerOptions();
            locationOptions.title(pharmacy.getName());
            locationOptions.position(drugstore);

            if(pharmacy.getOpen() == 1)
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            else if(pharmacy.getOpen() == 0)
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            else
                locationOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(locationOptions);
        }
    }
}
