package air1715.pillcare.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.database.entiteti.Pregled_Table;
import air1715.pillcare.DataLoaders.DataLoadController;
import air1715.pillcare.R;

public class PopisPregleda_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_pregleda);

        final Korisnik loggedUser = (Korisnik) getIntent().getSerializableExtra("korisnik");

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        DataLoadController dataControl = new DataLoadController(manager);
        List<Pregled> appointments = (List<Pregled>) dataControl.GetData("appointments", loggedUser);

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
