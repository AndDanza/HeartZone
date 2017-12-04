package air1715.pillcare.DataLoaders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Andrea on 29.11.2017.
 */

public class DataLoadController {
    private ConnectivityManager connection = null;

    public DataLoadController(ConnectivityManager connectionMobile) {
        this.connection = connectionMobile;
    }

    public Object GetData(String dataType){
        Object data = null;
        boolean isConnectedToInternet = false;
        DataLoader dataLoader = null;

        isConnectedToInternet = CheckInternetConnection();

        if(isConnectedToInternet == true){
            dataLoader = new WebServiceDataLoader();
        }
        else{
            dataLoader = new DatabaseDataLoader();
        }

        data = dataLoader.GetData(dataType);

        return data;
    }

    private boolean CheckInternetConnection() {
        NetworkInfo netInfo = connection.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
