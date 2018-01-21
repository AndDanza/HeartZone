package air1715.pillcare.Adapters;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;

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

    //singleton implementacija, ukoliko objekt postoji vrati ga, ako ne kreiraj novi
    public static ModularityController GetInstance(){
        if(controller == null)
            controller = new ModularityController();

        return controller;
    }

    //dodavanje opcije prikaza u listu mogućih prikaza
    public void AddModularOption(ModularRepresentation option){
        options.add(option);
    }

    //dohvati podatke o lijekovima i proizvođačima
    public void SetData(List<Lijek> medications, List<Proizvodac> companies){
        this.medications = medications;
        this.companies = companies;
    }

    //pomoću statičnog brojača izmjenjuju se prikazi
    public void ShowModularOption(){
        index++;

        if(index == options.size())
            index = 0;

        medicationPreview = options.get(index);

        medicationPreview.LoadData(medications, companies);
        medicationPreview.SetAdapter();
    }

    public void ClearData(){
        ModularRepresentation representation = options.get(index);
        representation.ClearData();
    }
}
