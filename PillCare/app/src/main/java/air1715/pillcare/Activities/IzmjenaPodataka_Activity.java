package air1715.pillcare.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Korisnik_Table;
import air1715.pillcare.R;
import air1715.pillcare.Utils.EncryptionUtils;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

public class IzmjenaPodataka_Activity extends AppCompatActivity {
    EditText email;
    EditText userName;
    EditText firstName;
    EditText lastName;
    EditText password;
    EditText repeatPassword;
    Korisnik loggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmjena_podataka);
        loggedUser = PrijavaActivity.getLoggedUser();

        email = (EditText) findViewById(R.id.input_ChangeEmail);
        userName = (EditText) findViewById(R.id.input_ChangeUserNameRegistration);
        firstName = (EditText) findViewById(R.id.input_ChangeFirstName);
        lastName = (EditText) findViewById(R.id.input_ChangeLastName);
        password = (EditText) findViewById(R.id.input_ChangePasswordRegistration);
        repeatPassword = (EditText) findViewById(R.id.input_ChangeRetypePassword);

        loadUserdata(loggedUser);

        Button buttonChange = (Button) findViewById(R.id.button_ChangeData);
        buttonChange.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (isNetworkAvailable() == true) {

                                                    changeData(loggedUser);

                                                } else {
                                                    Toast.makeText(IzmjenaPodataka_Activity.this, "Nemate pristup internetu, nije moguće promijeniti podatke", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
        );
    }

    private void loadUserdata(Korisnik korisnik) {

        email.setText(korisnik.getEmail());
        userName.setText(korisnik.getKorisnickoIme());
        firstName.setText(korisnik.getIme());
        lastName.setText(korisnik.getPrezime());

    }

    public void changeData(Korisnik loggedUser) {

        String newPassword = password.getText().toString();
        String newEmail = email.getText().toString();
        String newUserName = userName.getText().toString();
        String newFirstName = firstName.getText().toString();
        String newLastName = lastName.getText().toString();
        String newRepeatPassword = repeatPassword.getText().toString();

        if (newEmail.isEmpty() || newFirstName.isEmpty() || newLastName.isEmpty() || newUserName.isEmpty())
            Toast.makeText(IzmjenaPodataka_Activity.this, "Niste popunili sva polja", Toast.LENGTH_SHORT).show();
        else {
            loggedUser.setEmail(newEmail);
            loggedUser.setKorisnickoIme(newUserName);
            loggedUser.setIme(newFirstName);
            loggedUser.setPrezime(newLastName);

            if (!password.getText().toString().isEmpty() && !repeatPassword.getText().toString().isEmpty()) {


                if (newPassword.equals(newRepeatPassword)) {
                    try {
                        newPassword = EncryptionUtils.sha1(newPassword);
                        loggedUser.setLozinka(newPassword);
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(IzmjenaPodataka_Activity.this, "Došlo je do pogreške", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(IzmjenaPodataka_Activity.this, "Lozinka i potvrda lozinke moraju biti isti", Toast.LENGTH_SHORT).show();


            }
            updateUser(loggedUser);

        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateUser(Korisnik korisnik) {
        try {
            Map params = new HashMap<String, Object>();
            params.put("update", korisnik);
            HttpUtils.sendGetRequest(params, "https://pillcare.000webhostapp.com/dodajKorisnika.php");

            Toast.makeText(IzmjenaPodataka_Activity.this, "Uspješno ste promijenili podatke!", Toast.LENGTH_SHORT).show();

            PrijavaActivity.setLoggedUser(korisnik);

            Intent intent = new Intent(getBaseContext(), IzmjenaPodataka_Activity.class);
            intent.putExtra("korisnik", korisnik);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(IzmjenaPodataka_Activity.this, "Došlo je do pogreške", Toast.LENGTH_SHORT).show();
        }

    }
}