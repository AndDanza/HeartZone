package air1715.pillcare.Adapters;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

/**
 * The type Modularity controller.
 */
public class ModularityController {
    private static ModularityController controller = null;
    private static List<ModularRepresentation> options = null;

    /**
     * The Medications.
     */
    List<Lijek> medications;
    /**
     * The Companies.
     */
    List<Proizvodac> companies;

    /**
     * The Medication preview.
     */
    ModularRepresentation medicationPreview;

    private static int index;

    private ModularityController(){
        options = new ArrayList<ModularRepresentation>();
    }

    /**
     * Get instance modularity controller.
     *
     * @return the modularity controller
     */
//singleton implementacija, ukoliko objekt postoji vrati ga, ako ne kreiraj novi
    public static ModularityController GetInstance(){
        if(controller == null)
            controller = new ModularityController();

        return controller;
    }

    /**
     * Add modular option.
     *
     * @param option the option
     */
//dodavanje opcije prikaza u listu mogucih prikaza
    public void AddModularOption(ModularRepresentation option){
        options.add(option);
    }

    /**
     * Set data.
     *
     * @param medications the medications
     * @param companies   the companies
     */
//dohvati podatke o lijekovima i proizvodacima
    public void SetData(List<Lijek> medications, List<Proizvodac> companies){
        this.medications = medications;
        this.companies = companies;
    }

    /**
     * Show modular option.
     */
//pomocu staticnog brojaca izmjenjuju se prikazi
    public void ShowModularOption(){
        index++;

        if(index == options.size())
            index = 0;

        medicationPreview = options.get(index);

        medicationPreview.LoadData(medications, companies);
        medicationPreview.SetAdapter();
    }

    /**
     * Clear data.
     */
    public void ClearData(){
        ModularRepresentation representation = options.get(index);
        representation.ClearData();
    }
}
