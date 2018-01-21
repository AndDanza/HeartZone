package air1715.pillcare.DataLoaders;

import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;
import air1715.pillcare.Activities.PrijavaActivity;

/**
 * The type Database data loader.
 */
public class DatabaseDataLoader implements DataLoader {
    private static DatabaseDataLoader databaseLoader = null;

    private DatabaseDataLoader() {
    }

    /**
     * The Korisnik.
     */
    Korisnik korisnik = PrijavaActivity.getLoggedUser();

    /**
     * Get instance database data loader.
     *
     * @return the database data loader
     */
    public static DatabaseDataLoader GetInstance(){
        if(databaseLoader == null)
            databaseLoader = new DatabaseDataLoader();

        return databaseLoader;
    }

    @Override
    public Object GetData(String dataType, Object object) {
        return null;
    }

    @Override
    public List<Lijek> GetMedications() {
        return null;
    }

    @Override
    public List<Terapija> GetAllTherapies() {
        return null;
    }

    @Override
    public List<Proizvodac> GetPharmaCompanies() {
        return null;
    }

    @Override
    public List<Pregled> GetAppointments() {
        return null;
    }

    @Override
    public Terapija getSpecificTherapy(Object object) {
        return null;
    }

    @Override
    public Lijek getSpecificMedication(Object object) {
        return null;
    }
}
