package air1715.pillcare.Adapters;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

/**
 * Created by Andrea on 07.12.2017.
 */

public class ModularityController {
    private static ModularityController controller = null;
    private static List<ModularRepresentation> options = null;

    List<Lijek> medications;
    List<Proizvodac> companies;

    ModularRepresentation medicationPreview;

    private static int index;

    private ModularityController(){
        options = new ArrayList<ModularRepresentation>();
    }

    public static ModularityController GetInstance(){
        if(controller == null)
            controller = new ModularityController();

        return controller;
    }

    public void AddModularOption(ModularRepresentation option){
        options.add(option);
    }

    public void SetData(List<Lijek> medications, List<Proizvodac> companies){
        this.medications = medications;
        this.companies = companies;
    }

    public void ShowModularOption(){
        index++;

        if(index == options.size())
            index = 0;

        medicationPreview = options.get(index);

        medicationPreview.LoadData(medications, companies);
        medicationPreview.SetAdapter();
    }
}
