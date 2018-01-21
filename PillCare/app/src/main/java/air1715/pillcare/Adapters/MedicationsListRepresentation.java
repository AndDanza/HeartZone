package air1715.pillcare.Adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

/**
 * The type Medications list representation.
 */
public class MedicationsListRepresentation implements ModularRepresentation {
    /**
     * The Medications.
     */
    List<Lijek> medications = null;
    /**
     * The Companies.
     */
    List<Proizvodac> companies = null;
    /**
     * The Recycler.
     */
    View recycler;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Adapter.
     */
    MedicationsRecyclerAdapter adapter;

    /**
     * Instantiates a new Medications list representation.
     *
     * @param recycler the recycler
     * @param context  the context
     */
    public MedicationsListRepresentation(View recycler, Context context) {
        this.recycler = recycler;
        this.context = context;
    }

    @Override
    public void LoadData(List<Lijek> givenMedications, List<Proizvodac> givenComapnies) {
        this.medications = givenMedications;
        this.companies = givenComapnies;
    }

    @Override
    public void SetAdapter() {
        adapter = new MedicationsRecyclerAdapter(R.layout.medication_list_item, medications, companies);
        RecyclerView recyclerView = (RecyclerView) recycler;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void ClearData(){
        adapter.ClearData();
    }
}
