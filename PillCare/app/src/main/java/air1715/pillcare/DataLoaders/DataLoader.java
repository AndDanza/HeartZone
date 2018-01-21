package air1715.pillcare.DataLoaders;

import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;

public interface DataLoader {
    Object GetData(String dataType, Object object);

    List<Lijek> GetMedications();

    List<Terapija> GetAllTherapies();

    List<Proizvodac> GetPharmaCompanies();

    List<Pregled> GetAppointments();

    Terapija getSpecificTherapy(Object object);

    Lijek getSpecificMedication(Object object);
}
