package air1715.pillcare.Utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Marijan Hranj on 30/10/2017.
 */

public class HttpUtils {

    private final static String USER_AGENT = "Mozilla/5.0";


    public static JSONObject sendGetRequest(Map<String, String> params, String path) {
        JSONObject jsonResult=null;
        try {
            StringJoiner stringJoiner = new StringJoiner("&");
            for (Map.Entry<String, String> entry : params.entrySet())
                stringJoiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            path += "?" + stringJoiner.toString();
            HttpClient httpClient=new DefaultHttpClient();
            HttpGet request=new HttpGet(path);
            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Accept","application/json");
            request.addHeader("Content-Type", "application/json");
            HttpResponse response=httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK){
                HttpEntity entity=response.getEntity();
                String jsonResponse= EntityUtils.toString(entity);
                jsonResult=new JSONObject(jsonResponse);
            }

        }
        catch (IOException e) {
            System.out.println("IOException. " + e.getLocalizedMessage());
        }
        catch (JSONException e){
            System.out.println("JsonException. " + e.getLocalizedMessage());

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
