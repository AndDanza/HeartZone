package air1715.pillcare.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import air1715.database.entiteti.Korisnik;
import air1715.pillcare.R;

public class IzmjenaPodataka_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmjena_podataka);
        final Korisnik loggedUser = (Korisnik) getIntent().getSerializableExtra("korisnik");
        Button buttonChange = (Button) findViewById(R.id.button_ChangeData);
        buttonChange.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.d("email", loggedUser.getEmail());
                                                EditText newEmail = (EditText) findViewById(R.id.input_ChangeEmail);
                                                String Email = newEmail.getText().toString();
                                                loggedUser.setEmail(Email);
                                                Log.d("new email", loggedUser.getEmail());

                                            }
                                        }


        );

    }
}
