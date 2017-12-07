package air1715.pillcare.DataLoaders;

import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;

/**
 * Created by Andrea on 29.11.2017.
 */

public class DatabaseDataLoader implements DataLoader {

    @Override
    public List<Lijek> GetMedications() {
        return null;
    }

    @Override
    public List<Terapija> GetAllTherapies(Korisnik korisnik) {
        return null;
    }

    @Override
    public List<Proizvodac> GetPharmaCompanies() {
        return null;
    }

    @Override
    public List<Pregled> GetAppointments(Korisnik user) {
        return null;
    }

    @Override
    public Object GetData(String dataType, Korisnik user, Object object) {
        return null;
    }
}
