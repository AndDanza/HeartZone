package air1715.pillcare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class PrijavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijava);

        FlowManager.init(new FlowConfig.Builder(this).build());

        TextView openRegistrationActivity=(TextView) findViewById(R.id.txt_registracija);
        openRegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openActivity=new Intent(PrijavaActivity.this, Registracija_Activity.class);
                startActivity(openActivity);
            }
        });

        Button prijava=(Button) findViewById(R.id.button_prijava);
        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ekran=new Intent(getBaseContext(),PopisLijekova_Activity.class);
                startActivity(ekran);
            }
        });


    }

}
