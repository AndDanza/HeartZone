package air1715.pillcare.Adapters;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

/**
 * Created by Andrea on 06.12.2017.
 */

public interface ModularRepresentation {
    void LoadData(List<Lijek> givenMedications, List<Proizvodac> givenComapnies);
    void SetAdapter();
}
