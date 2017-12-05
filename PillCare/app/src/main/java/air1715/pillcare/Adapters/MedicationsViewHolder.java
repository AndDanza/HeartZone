package air1715.pillcare.Adapters;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.R;

/**
 * Created by Andrea on 05.12.2017.
 */

public class MedicationsViewHolder extends RecyclerView.ViewHolder{
    View mItemView;

    TextView medicationName;
    TextView medicationSize;
    TextView medicationStrength;
    TextView medicationCompany;
    ImageView medicationImage;

    public MedicationsViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        GetViewElements();
    }

    public void BindViewWithData(Lijek medication, Proizvodac company){
        medicationName.setText(medication.getNaziv());
        medicationSize.setText(String.valueOf(medication.getBrojTableta()));
        medicationStrength.setText(String.valueOf(medication.getJacina()));
        medicationCompany.setText(company.getNaziv());
        Picasso.with(itemView.getContext()).load(medication.getPakiranje()).into(medicationImage);
    }

    private void GetViewElements() {
        medicationName = (TextView) mItemView.findViewById(R.id.medication_name);
        medicationSize = (TextView) mItemView.findViewById(R.id.medications_size);
        medicationStrength = (TextView) mItemView.findViewById(R.id.medication_strenght);
        medicationCompany = (TextView) mItemView.findViewById(R.id.medication_company);
        medicationImage = (ImageView) mItemView.findViewById(R.id.medication_image);
    }

    public void MedicationSelected(){
        //TODO
        //implementirat da se na klik otvaraju detalji o lijeku
    }
}
