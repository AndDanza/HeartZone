package air1715.pillcare.Utils;


import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * The type Http utils.
 */
public class HttpUtils {

    private final static String USER_AGENT = "Mozilla/5.0";

    /**
     * Send get request json object.
     *
     * @param params the params
     * @param path   the path
     * @return the json object
     */
    public static JSONObject sendGetRequest(Map<String, Object> params, String path) {
        Gson gson = new Gson();
        JSONObject jsonResult = null;
        try {
            String paramatersString = "";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String jsonString;
                if (!entry.getValue().getClass().equals(String.class))
                    jsonString = gson.toJson(entry.getValue());
                else
                    jsonString = entry.getValue().toString();
                paramatersString += (URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(jsonString, "UTF-8")) + "&";
            }
            path += "?" + paramatersString.substring(0, paramatersString.length() - 1);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(path);
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);
                jsonResult = new JSONObject(jsonResponse);
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsuportedEndodingException. " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("IOException. " + e.getLocalizedMessage());
        } catch (JSONException e) {
            System.out.println("JsonExceptionHTTPUtils. " + e.getLocalizedMessage());
        }
        return jsonResult;
    }

    /**
     * Send get request array json array.
     *
     * @param params the params
     * @param path   the path
     * @return the json array
     */
    public static JSONArray sendGetRequestArray(Map<String, Object> params, String path) {
        Gson gson = new Gson();
        JSONArray jsonResult = null;
        JSONObject jsonObject = null;
        try {
            String paramatersString = "";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String jsonString;
                if (!entry.getValue().getClass().equals(String.class))
                    jsonString = gson.toJson(entry.getValue());
                else
                    jsonString = entry.getValue().toString();
                paramatersString += (URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(jsonString, "UTF-8")) + "&";
            }
            path += "?" + paramatersString.substring(0, paramatersString.length() - 1);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(path);
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);
                try{
                    jsonResult = new JSONArray(jsonResponse);
                } catch (JSONException e){
                    jsonObject = new JSONObject(jsonResponse);
                    jsonResult = new JSONArray();
                    jsonResult.put(jsonObject);
                }

            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsuportedEndodingException. " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("IOException. " + e.getLocalizedMessage());
        } catch (JSONException e) {
            System.out.println("JsonExceptionHTTPUtils. " + e.getLocalizedMessage());
        }
        return jsonResult;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
