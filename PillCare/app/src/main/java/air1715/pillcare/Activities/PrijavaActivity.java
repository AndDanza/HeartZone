package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.pillcare.R;
import air1715.pillcare.Utils.EncryptionUtils;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

/**
 * The type Prijava activity.
 */
public class PrijavaActivity extends AppCompatActivity {

    private static Korisnik loggedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijava);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FlowManager.init(new FlowConfig.Builder(this).build());

        final Context context = getApplicationContext();

        TextView openRegistrationActivity = (TextView) findViewById(R.id.txt_registracija);
        final EditText usernameET = (EditText) findViewById(R.id.input_username);
        final EditText passwordET = (EditText) findViewById(R.id.input_password);
        final Map params = new HashMap<String, String>();

        openRegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openActivity = new Intent(PrijavaActivity.this, Registracija_Activity.class);
                startActivity(openActivity);
            }
        });

        Button prijava = (Button) findViewById(R.id.button_prijava);
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                if (username.equals("")) {
                    PopUpUtils.sendMessage(context, "Morate unijeti korisničko ime");
                } else if (password.equals("")) {
                    PopUpUtils.sendMessage(context, "Morate unijeti lozinku");
                } else {
                    params.put("username", username);
                    JSONObject response = HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/korisnik.php");
                    if (response == null) {
                        PopUpUtils.sendMessage(context, "Ne postoji korisnik s navedenim korisničkim imenom ili nemate internet konekciju");
                    } else {
                        try {
                            Korisnik korisnik = new Korisnik(response);
                            String encryptedPw = EncryptionUtils.sha1(password);
                            if (encryptedPw.equals(korisnik.getLozinka())) {
                                loggedUser = korisnik;
                                Intent intent = new Intent(getBaseContext(), PopisLijekova_Activity.class);
                                startActivity(intent);
                            } else {
                                PopUpUtils.sendMessage(context, "Pogresna lozinka");
                            }
                        } catch (JSONException e) {
                            System.out.println("JsonException. " + e.getLocalizedMessage());
                            PopUpUtils.sendMessage(context, "Doslo je do pogreske u radu aplikacije");
                        } catch(NoSuchAlgorithmException e){
                            System.out.println("NoSuchAlgorithmExcpetion. " + e.getLocalizedMessage());
                            PopUpUtils.sendMessage(context, "Doslo je do pogreske u radu aplikacije");
                        }
                    }

                }
            }
        });


    }

    /**
     * Gets logged user.
     *
     * @return the logged user
     */
    public static Korisnik getLoggedUser() {
        return loggedUser;
    }

    /**
     * Set logged user.
     *
     * @param newUser the new user
     */
    public static void setLoggedUser(Korisnik newUser){
        loggedUser = newUser;
    }
}
