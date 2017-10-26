package air1715.pillcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrijavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijava);

        TextView openRegistrationActivity=(TextView) findViewById(R.id.txt_registracija);
        openRegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openActivity=new Intent(PrijavaActivity.this, Registracija_Activity.class);
                startActivity(openActivity);
            }
        });


    }

}
