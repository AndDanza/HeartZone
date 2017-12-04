package air1715.pillcare.DataLoaders;

import android.preference.PreferenceGroup;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;

/**
 * Created by Andrea on 29.11.2017.
 */

public interface DataLoader {
    Object GetData(String dataType);
    List<Lijek> GetMedications();
    List<Terapija> GetTeraphies();
    List<Proizvodac> GetPhramaCompanies();
    List<Pregled> GetAppointments();
}