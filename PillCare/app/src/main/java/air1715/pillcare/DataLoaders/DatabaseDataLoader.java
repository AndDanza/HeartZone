package air1715.pillcare.DataLoaders;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;

/**
 * Created by Andrea on 29.11.2017.
 */

public class DatabaseDataLoader implements DataLoader {
    @Override
    public Object GetData(String dataType) {
        return null;
    }

    @Override
    public List<Lijek> GetMedications() {
        return null;
    }

    @Override
    public List<Terapija> GetTeraphies() {
        return null;
    }

    @Override
    public List<Proizvodac> GetPhramaCompanies() {
        return null;
    }

    @Override
    public List<Pregled> GetAppointments() {
        return null;
    }
}
