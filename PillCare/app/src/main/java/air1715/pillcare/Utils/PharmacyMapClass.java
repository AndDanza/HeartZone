package air1715.pillcare.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type Pharmacy map class.
 */
public class PharmacyMapClass {
    private double latitude;
    private double longitude;
    private int open;
    private String name;
    private String address;

    /**
     * Instantiates a new Pharmacy map class.
     *
     * @param jsonObject the json object
     * @throws JSONException the json exception
     */
    public PharmacyMapClass(JSONObject jsonObject) throws JSONException {
        this.latitude = Double.valueOf(jsonObject.getString("latitude"));
        this.longitude = Double.valueOf(jsonObject.getString("longitude"));
        this.open = jsonObject.getInt("open");
        this.name = jsonObject.getString("name");
        this.address = jsonObject.getString("address");
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets open.
     *
     * @return the open
     */
    public int getOpen() { return open; }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {return address;}

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
