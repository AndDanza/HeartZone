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

public interface DataLoader {
    Object GetData(String dataType, Korisnik user, Object object);
    List<Lijek> GetMedications();
    List<Terapija> GetAllTherapies(Korisnik korisnik);
    List<Proizvodac> GetPharmaCompanies();
    List<Pregled> GetAppointments(Korisnik user);
}
