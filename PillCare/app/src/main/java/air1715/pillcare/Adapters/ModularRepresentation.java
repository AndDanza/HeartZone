package air1715.pillcare.Adapters;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

/**
 * The interface Modular representation.
 */
public interface ModularRepresentation {
    /**
     * Load data.
     *
     * @param givenMedications the given medications
     * @param givenComapnies   the given comapnies
     */
    void LoadData(List<Lijek> givenMedications, List<Proizvodac> givenComapnies);

    /**
     * Set adapter.
     */
    void SetAdapter();

    /**
     * Clear data.
     */
    void ClearData();
}
