package air1715.pillcare.DataLoaders;

import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Proizvodac;
import air1715.database.entiteti.Terapija;

/**
 * The interface Data loader.
 */
public interface DataLoader {
    /**
     * Get data object.
     *
     * @param dataType the data type
     * @param object   the object
     * @return the object
     */
    Object GetData(String dataType, Object object);

    /**
     * Get medications list.
     *
     * @return the list
     */
    List<Lijek> GetMedications();

    /**
     * Get all therapies list.
     *
     * @return the list
     */
    List<Terapija> GetAllTherapies();

    /**
     * Get pharma companies list.
     *
     * @return the list
     */
    List<Proizvodac> GetPharmaCompanies();

    /**
     * Get appointments list.
     *
     * @return the list
     */
    List<Pregled> GetAppointments();

    /**
     * Gets specific therapy.
     *
     * @param object the object
     * @return the specific therapy
     */
    Terapija getSpecificTherapy(Object object);

    /**
     * Gets specific medication.
     *
     * @param object the object
     * @return the specific medication
     */
    Lijek getSpecificMedication(Object object);
}
