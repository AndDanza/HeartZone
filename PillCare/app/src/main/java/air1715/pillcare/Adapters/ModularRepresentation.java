package air1715.pillcare.Adapters;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

public interface ModularRepresentation {
    void LoadData(List<Lijek> givenMedications, List<Proizvodac> givenComapnies);

    void SetAdapter();

    void ClearData();
}
