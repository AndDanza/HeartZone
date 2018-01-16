package air1715.pillcare.DataLoaders;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.Activities.PrijavaActivity;
import air1715.pillcare.Utils.HttpUtils;

/**
 * Created by Andrea on 29.11.2017.
 */

public class WebServiceDataLoader implements DataLoader {
    private static WebServiceDataLoader webLoader = null;

    private WebServiceDataLoader() {
    }

    Korisnik korisnik = PrijavaActivity.getLoggedUser();

    @Override
    public Object GetData(String dataType, Object object) {
        Object returnData = null;

        switch (dataType) {
            case "medications":
                returnData = (Object) GetMedications();
                break;
            case "allTherapies":
                returnData = (Object) GetAllTherapies();
                break;
            case "pharmaCompanies":
                returnData = (Object) GetPharmaCompanies();
                break;
            case "appointments":
                returnData = (Object) GetAppointments();
                break;
            case "specificTherapy":
                returnData = (Object) getSpecificTherapy(object);
                break;
            case "specificMed":
                returnData = (Object) getSpecificMedication(object);
                break;
        }

        return returnData;
    }

    public static WebServiceDataLoader GetInstance(){
        if(webLoader == null)
            webLoader = new WebServiceDataLoader();

        return webLoader;
    }

    @Override
    public List<Lijek> GetMedications() {
        Map params = new HashMap<String, String>();
        params.put("user_id", korisnik.getId());
        JSONArray response = HttpUtils.sendGetRequestArray(params, "https://pillcare.000webhostapp.com/lijekovi.php");
        List<Lijek> medications = new ArrayList<Lijek>();
        try {
            if(response != null) {
                Log.d("response", "response razlicit od null medications ");
                medications = getMedicationsJSON(medications, response);
            }
            else {
                Log.d("null", "null u response-u medications");
                return null;
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionMedications. " + e.getLocalizedMessage());
        }

        return medications;

    }

    private List<Lijek> getMedicationsJSON(List<Lijek> medications, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            medications.add(new Lijek(jsonobject));
        }

        return medications;
    }

    @Override
    public List<Terapija> GetAllTherapies(){
        return null;
    }

    @Override
    public List<Proizvodac> GetPharmaCompanies() {
        Map params = new HashMap<String, String>();
        params.put("type", "all");
        JSONArray response = HttpUtils.sendGetRequestArray(params, "https://pillcare.000webhostapp.com/proizvodac.php");
        List<Proizvodac> companies = new ArrayList<Proizvodac>();
        try {
            if(response != null) {
                Log.d("response", "response razlicit od null companies");
                companies = getCompaniesJSON(companies, response);
            }
            else {
                Log.d("null", "null u response-u companies");
                return null;
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionCompanies. " + e.getLocalizedMessage());
        }

        return companies;
    }

    private List<Proizvodac> getCompaniesJSON(List<Proizvodac> companies, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            companies.add(new Proizvodac(jsonobject));
        }

        return companies;
    }

    @Override
    public List<Pregled> GetAppointments() {
        Map params = new HashMap<String, String>();
        params.put("user", korisnik.getKorisnickoIme());
        JSONArray response = HttpUtils.sendGetRequestArray(params, "https://pillcare.000webhostapp.com/pregled.php");
        List<Pregled> appointments = new ArrayList<Pregled>();
        try {
            if(response != null) {
                Log.d("response", "response razlicit od null pregledi");
                appointments = getAppointmentsJSON(appointments, response);
            }
            else {
                Log.d("null", "null u response-u appointments");
                return null;
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionAppointments. " + e.getLocalizedMessage());
        }

        return appointments;
    }

    private List<Pregled> getAppointmentsJSON(List<Pregled> appointments, JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            appointments.add(new Pregled(jsonobject));
        }

        return appointments;
    }

    public Terapija getSpecificTherapy(Object medicForUser) {
        List<Object> parameters = (List<Object>) medicForUser;

        Map params = new HashMap<String, String>();
        params.put("user", (Korisnik) parameters.get(1));
        params.put("medicament", (Lijek) parameters.get(0));
        params.put("active", "1");
        JSONObject response = HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/specificnaTerapija.php");
        Terapija terapija = null;
        try {
            if(response != null) {
                Log.d("response", "response razlicit od null pregledi");
                terapija = new Terapija(response);
            }
            else {
                Log.d("null", "null u response-u appointments");
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionAppointments. " + e.getLocalizedMessage());
        }

        return terapija;
    }

    @Override
    public Lijek getSpecificMedication(Object object) {
        String med_id = (String) object;

        Map params = new HashMap<String, String>();
        params.put("med_id", med_id);
        JSONObject response = HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/traziLijek.php");
        Lijek medication = null;
        try {
            if(response != null) {
                Log.d("response", "response razlicit od null pregledi");
                medication = new Lijek(response);
            }
            else {
                Log.d("null", "null u response-u appointments");
            }
        }
        catch (JSONException e) {
            System.out.println("JsonExceptionAppointments. " + e.getLocalizedMessage());
        }

        return medication;
    }
}
