package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.pillcare.R;
import air1715.pillcare.Utils.EncryptionUtils;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

public class Registracija_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija_);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

        final Context context = getApplicationContext();

        final EditText firstNameET = (EditText) findViewById(R.id.input_FirstName);
        final EditText lastNameET = (EditText) findViewById(R.id.input_LastName);
        final EditText usernameET = (EditText) findViewById(R.id.input_UserNameRegistration);
        final EditText emailET = (EditText) findViewById(R.id.input_Email);
        final EditText passwordET = (EditText) findViewById(R.id.input_PasswordRegistration);
        final EditText retypePasswordET = (EditText) findViewById(R.id.input_RetypePassword);
        final Button submitReg = (Button) findViewById(R.id.button_registration);


        submitReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameET.getText().toString();
                final String lastName = lastNameET.getText().toString();
                final String username = usernameET.getText().toString();
                final String email = emailET.getText().toString();
                final String password = passwordET.getText().toString();
                final String retypePassword = retypePasswordET.getText().toString();
                if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty())
                    PopUpUtils.sendMessage(context, "Morate popuniti sva polja");
                else if (!password.equals(retypePassword))
                    PopUpUtils.sendMessage(context, "Lozinka i potvrda lozinke moraju biti isti");
                else if (existsUsernameOrEmail(username, email))
                    PopUpUtils.sendMessage(context, "Postoji već korisnik s navedenim korisničkim imenom ili e-mailom");
                else{
                    try {
                        Korisnik user = new Korisnik(firstName, lastName, email, username, EncryptionUtils.sha1(password));
                        Map params = new HashMap<String, Object>();
                        params.put("user", user);
                        if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/dodajKorisnika.php") != null) {
                            PopUpUtils.sendMessage(context, "Uspješno ste se registrirali");
                            Intent intent = new Intent(getBaseContext(), PrijavaActivity.class);
                            startActivity(intent);
                        } else
                            PopUpUtils.sendMessage(context, "Problem prilikom spremanja korisnika u bazu");
                    } catch (NoSuchAlgorithmException e) {
                        PopUpUtils.sendMessage(context, "Doslo je do pogreske u radu aplikacije");
                    }
                }
            }
        });


    }

    private boolean existsUsernameOrEmail(String username, String password) {
        Map params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("email", password);
        if (HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/provjeriKorisnika.php") != null)
            return true;
        return false;
    }
}
