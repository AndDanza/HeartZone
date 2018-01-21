package air1715.pillcare.DataLoaders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;

import java.net.InetAddress;
import java.util.List;

import air1715.database.entiteti.Korisnik;

/**
 * The type Data load controller.
 */
public class DataLoadController {
    private static ConnectivityManager connection = null;
    private static DataLoadController controller = null;

    private DataLoadController() {
    }

    /**
     * Get instance data load controller.
     *
     * @param manager the manager
     * @return the data load controller
     */
    public static DataLoadController GetInstance(ConnectivityManager manager){
        connection = manager;

        if(controller == null)
            controller = new DataLoadController();

        return controller;
    }

    /**
     * Get data object.
     *
     * @param dataType the data type
     * @param object   the object
     * @return the object
     */
    public Object GetData(String dataType, Object object){
        Object data = null;
        boolean isConnectedToInternet = false;
        DataLoader dataLoader = null;

        isConnectedToInternet = CheckInternetConnection();

        if(isConnectedToInternet == true){
            dataLoader = WebServiceDataLoader.GetInstance();
        }
        else{
            dataLoader = DatabaseDataLoader.GetInstance();
        }

        data = dataLoader.GetData(dataType, object);

        return data;
    }

    private boolean CheckInternetConnection() {
        NetworkInfo netInfo = connection.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
