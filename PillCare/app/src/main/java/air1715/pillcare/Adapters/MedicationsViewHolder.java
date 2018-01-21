package air1715.pillcare.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import air1715.database.entiteti.Lijek;
import air1715.database.entiteti.Proizvodac;
import air1715.pillcare.Activities.TerapijaActivity;
import air1715.pillcare.R;

/**
 * The type Medications view holder.
 */
public class MedicationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    /**
     * The M item view.
     */
    View mItemView;

    /**
     * The Medication name.
     */
    TextView medicationName;
    /**
     * The Medication size.
     */
    TextView medicationSize;
    /**
     * The Medication strength.
     */
    TextView medicationStrength;
    /**
     * The Medication company.
     */
    TextView medicationCompany;
    /**
     * The Medication image.
     */
    ImageView medicationImage;

    /**
     * The Medication.
     */
    Lijek medication;
    /**
     * The Company.
     */
    Proizvodac company;

    /**
     * Instantiates a new Medications view holder.
     *
     * @param itemView the item view
     */
    public MedicationsViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mItemView.setOnClickListener(this);
        GetViewElements();
    }

    /**
     * Bind view with data.
     *
     * @param medication the medication
     * @param company    the company
     */
    public void BindViewWithData(Lijek medication, Proizvodac company){
        this.medication = medication;
        this.company = company;

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

    @Override
    public void onClick(View v) {
        Log.d("click", "click happened");
        Intent intent = new Intent(v.getContext(), TerapijaActivity.class);
        intent.putExtra("medication", medication);
        intent.putExtra("company", company);
        v.getContext().startActivity(intent);
    }
}
