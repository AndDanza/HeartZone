package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.pillcare.R;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

public class NoviPregled_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_pregled);

        final EditText terminDatumVrijeme = (EditText) findViewById(R.id.inputTerminDatumVrijeme);
        final EditText biljeska = (EditText) findViewById(R.id.inputBiljeska);
        final EditText upozorenjeDatumVrijeme = (EditText) findViewById(R.id.inputUpozorenjeDatumVrijeme);
        final Map params = new HashMap<String, String>();

        final Context context = getApplicationContext();

        Button actionDodajPregled = (Button) findViewById(R.id.btnDodajPregled);
        actionDodajPregled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String terminTekst = terminDatumVrijeme.getText().toString();
                String biljeskaTekst = biljeska.getText().toString();
                String upozorenjeTekst = upozorenjeDatumVrijeme.getText().toString();

                if (terminTekst.isEmpty() || upozorenjeTekst.isEmpty()) {
                    PopUpUtils.sendMessage(context, "Sva polja osim bilješke su obavezna!");
                }
                else{
                    try {
                        //hardcodirano je postavljanje AKTIVNOST pregleda (true/false) i LOGIRANI KORISNIK

                        Pregled appointment = new Pregled(terminTekst,biljeskaTekst,upozorenjeTekst,true,3);
                        Map params = new HashMap<String, Object>();
                        params.put("appointment", appointment);
                        if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/dodajPregled.php") != null) {
                            PopUpUtils.sendMessage(context, "Uspješno ste se unijeli novi pregled!");
                        } else
                            PopUpUtils.sendMessage(context, "Problem prilikom spremanja novog pregleda u bazu!");
                    } catch (Exception e) {
                        PopUpUtils.sendMessage(context, "Doslo je do pogreske u radu aplikacije!");
                    }
                }
            }
        });
    }
}
