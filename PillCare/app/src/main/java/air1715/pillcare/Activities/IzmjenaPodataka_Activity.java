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

import air1715.database.entiteti.Korisnik;
import air1715.pillcare.R;

public class IzmjenaPodataka_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmjena_podataka);
        if(isNetworkAvailable()==true) {
            changeData();
        }
        else  {
        Toast.makeText(IzmjenaPodataka_Activity.this,"Nemate pristup internetu, nije moguće promijeniti podatke",Toast.LENGTH_SHORT).show();
        }



    }

    private void changeData() {

        final Korisnik loggedUser = PrijavaActivity.getLoggedUser();
        final EditText email = (EditText) findViewById(R.id.input_ChangeEmail);
        final EditText userName=(EditText) findViewById(R.id.input_ChangeUserNameRegistration);
        final EditText firstName=(EditText) findViewById(R.id.input_ChangeFirstName);
        final EditText lastName=(EditText) findViewById(R.id.input_ChangeLastName);
        final EditText password=(EditText) findViewById(R.id.input_ChangePasswordRegistration);
        final EditText repeatPassword=(EditText) findViewById(R.id.input_ChangeRetypePassword);
        Button buttonChange = (Button) findViewById(R.id.button_ChangeData);
        buttonChange.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.d("email", loggedUser.getEmail());
                                                String newEmail = email.getText().toString();
                                                loggedUser.setEmail(newEmail);
                                                Log.d("new email", loggedUser.getEmail());

                                                String newUserName=userName.getText().toString();
                                                loggedUser.setKorisnickoIme(newUserName);
                                                Log.d("new user",loggedUser.getKorisnickoIme());

                                                String newFirstName=firstName.getText().toString();
                                                loggedUser.setIme(newFirstName);

                                                String newLastName=lastName.getText().toString();
                                                loggedUser.setPrezime(newLastName);

                                                String newPassword=password.getText().toString();
                                                String newRepeatPassword=repeatPassword.getText().toString();

                                                if(newPassword.equals(newRepeatPassword)){

                                                    loggedUser.setLozinka(newRepeatPassword);
                                                    Log.d("new pass",loggedUser.getLozinka());
                                                    Toast.makeText(IzmjenaPodataka_Activity.this,"Uspješno ste promijenili podatke",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(IzmjenaPodataka_Activity.this,"Lozinka i potvrda lozinke moraju biti isti",Toast.LENGTH_SHORT).show();

                                                loggedUser.update();

                                            }
                                        }


        );

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
