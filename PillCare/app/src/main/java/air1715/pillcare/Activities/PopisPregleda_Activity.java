package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

public class PopisPregleda_Activity extends AppCompatActivity {

    Korisnik loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_pregleda);

        loggedUser = PrijavaActivity.getLoggedUser();

        FillWithData();

        Button openNoviPregledActivity = (Button) findViewById(R.id.btnDodajNoviPregled);
        openNoviPregledActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openActivity = new Intent(PopisPregleda_Activity.this, NoviPregled_Activity.class);
                startActivity(openActivity);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FillWithData();
    }

    private void FillWithData(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = DataLoadController.GetInstance(manager);
        List<Pregled> appointments = (List<Pregled>) dataControl.GetData("appointments", loggedUser, null);

        ListView listViewAppointments=(ListView) findViewById(R.id.listViewPregledi);

        if (appointments!=null) {
            ArrayAdapter<Pregled> adapter = new ArrayAdapter<Pregled>(this, android.R.layout.simple_list_item_1, appointments);
            listViewAppointments.setAdapter(adapter);
        }
        else{
            Toast toast = Toast.makeText(this, "Niste unijeli nijedan pregled!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}
