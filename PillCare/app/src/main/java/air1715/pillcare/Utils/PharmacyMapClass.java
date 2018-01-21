package air1715.pillcare.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PharmacyMapClass {
    private double latitude;
    private double longitude;
    private int open;
    private String name;
    private String address;

    public PharmacyMapClass(JSONObject jsonObject) throws JSONException {
        this.latitude = Double.valueOf(jsonObject.getString("latitude"));
        this.longitude = Double.valueOf(jsonObject.getString("longitude"));
        this.open = jsonObject.getInt("open");
        this.name = jsonObject.getString("name");
        this.address = jsonObject.getString("address");
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getOpen() { return open; }

    public String getAddress() {return address;}

    public String getName() {
        return name;
    }
}
