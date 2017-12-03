package air1715.pillcare.DataLoaders;

import android.provider.ContactsContract;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Andrea on 29.11.2017.
 */

public class DataLoadController {

    public DataLoadController() {
    }

    public Object GetData(String dataType){
        Object data = null;
        boolean isConnectedToInternet = false;
        DataLoader dataLoader = null;

        isConnectedToInternet = true;

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
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        }
        catch (Exception e) {
            return false;
        }
    }
}
