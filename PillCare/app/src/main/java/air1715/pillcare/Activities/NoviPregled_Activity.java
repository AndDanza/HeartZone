package air1715.pillcare.Activities;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import air1715.database.entiteti.Korisnik;
import air1715.database.entiteti.Pregled;
import air1715.pillcare.R;
import air1715.pillcare.Utils.HttpUtils;
import air1715.pillcare.Utils.PopUpUtils;

public class NoviPregled_Activity extends AppCompatActivity {

    TextView inputTerminDatum;
    TextView inputTerminVrijeme;
    TextView inputUpozorenjeDatum;
    TextView inputUpozorenjeVrijeme;
    Calendar mCurrentDate;
    int dayTerminUpozorenje,monthTerminUpozorenje,yearTerminUpozorenje,hourTerminUpozorenje,minuteTerminUpozorenje;
    Korisnik loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_pregled);

        final EditText biljeska = (EditText) findViewById(R.id.inputBiljeska);
        final Map params = new HashMap<String, String>();

        final Context context = getApplicationContext();
        loggedUser = PrijavaActivity.getLoggedUser();

        inputTerminDatum = (TextView) findViewById(R.id.inputTerminDatum);
        inputTerminVrijeme = (TextView) findViewById(R.id.inputTerminVrijeme);
        mCurrentDate = Calendar.getInstance();

        dayTerminUpozorenje = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        monthTerminUpozorenje = mCurrentDate.get(Calendar.MONTH);
        monthTerminUpozorenje=monthTerminUpozorenje+1;
        yearTerminUpozorenje = mCurrentDate.get(Calendar.YEAR);
        hourTerminUpozorenje = mCurrentDate.get(Calendar.HOUR_OF_DAY);
        minuteTerminUpozorenje = mCurrentDate.get(Calendar.MINUTE);

        inputTerminDatum.setText(dayTerminUpozorenje+"-"+monthTerminUpozorenje+"-"+yearTerminUpozorenje);
        inputTerminVrijeme.setText(hourTerminUpozorenje+":"+minuteTerminUpozorenje+":00");

        inputTerminDatum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoviPregled_Activity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        inputTerminDatum.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
                    }
                }, yearTerminUpozorenje, monthTerminUpozorenje, dayTerminUpozorenje);
                datePickerDialog.show();
            }
        });

        inputTerminVrijeme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoviPregled_Activity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputTerminVrijeme.setText(hourOfDay+":"+minute+":00");
                    }
                }, hourTerminUpozorenje,minuteTerminUpozorenje,false);
                timePickerDialog.show();
            }
        });


        inputUpozorenjeDatum = (TextView) findViewById(R.id.inputUpozorenjeDatum);
        inputUpozorenjeVrijeme = (TextView) findViewById(R.id.inputUpozorenjeVrijeme);

        inputUpozorenjeDatum.setText(dayTerminUpozorenje+"-"+monthTerminUpozorenje+"-"+yearTerminUpozorenje);
        inputUpozorenjeVrijeme.setText(hourTerminUpozorenje+":"+minuteTerminUpozorenje+":00");

        inputUpozorenjeDatum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoviPregled_Activity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        inputUpozorenjeDatum.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
                    }
                }, yearTerminUpozorenje, monthTerminUpozorenje, dayTerminUpozorenje);
                datePickerDialog.show();
            }
        });

        inputUpozorenjeVrijeme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoviPregled_Activity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputUpozorenjeVrijeme.setText(hourOfDay+":"+minute+":00");
                    }
                }, hourTerminUpozorenje,minuteTerminUpozorenje,false);
                timePickerDialog.show();
            }
        });


        Button actionDodajPregled = (Button) findViewById(R.id.btnDodajPregled);
        actionDodajPregled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        final String currentDateTime = dayTerminUpozorenje+"-"+monthTerminUpozorenje+"-"+yearTerminUpozorenje+" "+hourTerminUpozorenje+":"+minuteTerminUpozorenje+":0";
                        final String selectedDateTimeTermin = inputTerminDatum.getText().toString()+" "+inputTerminVrijeme.getText().toString();
                        final String selectedDateTimeUpozorenje = inputUpozorenjeDatum.getText().toString()+" "+inputUpozorenjeVrijeme.getText().toString();

                        Date dateTime=null;
                        Date dateTimeUpozorenje=null;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            dateTime=sdf.parse(currentDateTime);
                        } catch (ParseException e) {
                            Log.d("Greska: ","Pretvaranje stringa u datum -> dateTime");
                        }
                        try {
                            dateTimeUpozorenje=sdf.parse(selectedDateTimeUpozorenje);
                        } catch (ParseException e) {
                            Log.d("Greska: ","Pretvaranje stringa u datum -> dateTimeUpozorenje");
                        }

                        final boolean aktivan;
                        if (dateTimeUpozorenje.compareTo(dateTime)<0){
                            aktivan=false;
                        }
                        else {
                            aktivan = true;
                        }

                        String biljeskaTekst = biljeska.getText().toString();
                        Pregled appointment = new Pregled(selectedDateTimeTermin,biljeskaTekst,selectedDateTimeUpozorenje,aktivan, loggedUser.getId());

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
        });
    }


}
