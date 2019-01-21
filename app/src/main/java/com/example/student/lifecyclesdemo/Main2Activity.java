package com.example.student.lifecyclesdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class Main2Activity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    private int _xDelta;
    private int _yDelta;
    private int index;
    private String ulozenka;

    Button prevod,prevod2,prevod3,prevodMena;
    EditText editTextDec,editText2,editText3,editMena;
    TextView vystup1,vystup2,vystup3,usd,eur,czk,gbp;
    Spinner spinner,spinner2,spinner3,spinMeny;
    ArrayAdapter<CharSequence> adapter,adapter2,adapter3,adapterMena;
    LinearLayout tabulka;

    private int cislo,cislo2,cislo3;
    private String bin;
    private double inputValue;
    private String result[]= new String[10];
    boolean parsed=false;
    boolean ulozeno=false;

    DecimalFormat dec = new DecimalFormat("#.######");
    private SharedPreferences nPreferences;
    private SharedPreferences.Editor nEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nEditor= nPreferences.edit();

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        prevod = (Button) findViewById(R.id.button19);
        prevod2 = (Button) findViewById(R.id.button20);
        prevod3 = (Button) findViewById(R.id.button21);
        prevodMena = (Button) findViewById(R.id.button22);

        editTextDec = (EditText) findViewById(R.id.editText5);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editMena = (EditText) findViewById(R.id.editText);

        vystup1 = (TextView) findViewById(R.id.textView);
        vystup2 = (TextView) findViewById(R.id.textView3);
        vystup3 = (TextView) findViewById(R.id.textView6);
        usd = (TextView) findViewById(R.id.textView8);
        eur = (TextView) findViewById(R.id.textView7);
        czk = (TextView) findViewById(R.id.textView9);
        gbp = (TextView) findViewById(R.id.textView10);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinMeny = (Spinner) findViewById(R.id.spinner4);

        tabulka = (LinearLayout) findViewById(R.id.linearLayout2);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.typy_prevodu, android.R.layout.simple_spinner_item);

        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.prevodHex, android.R.layout.simple_spinner_item);

        adapter3 = ArrayAdapter.createFromResource(this,
                R.array.prevodBinHex, android.R.layout.simple_spinner_item);

        adapterMena = ArrayAdapter.createFromResource(this,
                R.array.mena, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMena.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinMeny.setAdapter(adapterMena);

        ulozenka=nPreferences.getString(getString(R.string.kontrola), "");
        if( ulozenka.equals("provedeno")==true){
            ulozeno=true;
            new calculate().execute();
        }

        prevod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vyber = spinner.getSelectedItem().toString();
                decimalBin(vyber);
            }
        });

        prevod2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vyber = spinner2.getSelectedItem().toString();
                decimalHex(vyber);
            }
        });

        prevod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vyber = spinner3.getSelectedItem().toString();
                HexBin(vyber);
            }
        });

        prevodMena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parsed=false;

                if(editMena.getText().toString().trim().length()> 0 && !editMena.getText().toString().trim().equals("."));
                String textValue= editMena.getText().toString();
                try {
                    inputValue = Double.parseDouble(textValue);
                    eur.setText("Čekám");
                    usd.setText("Čekám");
                    czk.setText("Čekám");
                    gbp.setText("Čekám");
                    parsed=true;
                }catch(NumberFormatException e){
                    eur.setText("Not a number");
                }
                if(parsed==true) {
                    ulozeno=false;
                    new calculate().execute();
                }
            }
        });

        spinMeny.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*tabulka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mozny toast
            }
        });*/

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            tabulka.setOnTouchListener(new View.OnTouchListener() {
                @Override

                public boolean onTouch (View view, MotionEvent event){
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                            _yDelta = Y - lParams.topMargin;
                            //_yDelta = Y - lParams.bottomMargin;
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.topMargin = 0;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = Y - _yDelta;
                            view.setLayoutParams(layoutParams);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void RetrieveSharedPreferences(){
        String indexx= nPreferences.getString(getString(R.string.volba), "");
        String vstup= nPreferences.getString(getString(R.string.hodnota), "");
        String vec11= nPreferences.getString(getString(R.string.vec1), "");
        String vec22= nPreferences.getString(getString(R.string.vec2), "");
        String vec33= nPreferences.getString(getString(R.string.vec3), "");

        inputValue=Double.parseDouble(vstup);
        index= Integer.parseInt(indexx);
        result[0]=vec11;
        result[1]=vec22;
        result[2]=vec33;
    }

    public class calculate extends AsyncTask<String,String,String[]> {

        public String getJson(String url) throws ClientProtocolException, IOException {
            StringBuilder build = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;
            while ((con = reader.readLine()) != null) {
                build.append(con);
            }
            return build.toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {

            if (index == 0) {
                double usdToEurKurz, usdToCzkKurz, usdToEurVysledek, usdToCzkVysledek, usdToUsdHodnota,usdToGbpKurz,usdToGbpVysledek;
                usdToUsdHodnota = inputValue * 1;
                usd.setText("USD: " + dec.format(usdToUsdHodnota));

                usdToEurKurz = Double.parseDouble(result[0]);
                usdToEurVysledek = inputValue * usdToEurKurz;
                eur.setText("EUR: " + dec.format(usdToEurVysledek));

                usdToCzkKurz = Double.parseDouble(result[1]);
                usdToCzkVysledek = inputValue * usdToCzkKurz;
                czk.setText("CZK: " + dec.format(usdToCzkVysledek));

                usdToGbpKurz = Double.parseDouble(result[2]);
                usdToGbpVysledek = inputValue * usdToGbpKurz;
                gbp.setText("GBP: " + dec.format(usdToGbpVysledek));

            } else if (index == 1) {
                double eurToUsdKurz, eurToCzkKurz, eurToUsdVysledek, eurToCzkVysledek, eurHodnota, eurToGbpKurz,eurToGbpVysledek;

                eurHodnota = inputValue * 1;
                eur.setText("EUR: " + dec.format(eurHodnota));

                eurToUsdKurz = Double.parseDouble(result[0]);
                eurToUsdVysledek = inputValue * eurToUsdKurz;
                usd.setText("USD: " + dec.format(eurToUsdVysledek));

                eurToCzkKurz = Double.parseDouble(result[1]);
                eurToCzkVysledek = inputValue * eurToCzkKurz;
                czk.setText("CZK: " + dec.format(eurToCzkVysledek));

                eurToGbpKurz = Double.parseDouble(result[2]);
                eurToGbpVysledek = inputValue * eurToGbpKurz;
                gbp.setText("GBP: " + dec.format(eurToGbpVysledek));

            } else if (index == 2) {
                double CzkToUsdKurz, CzkToEurKurz, CzkToUsdVysledek, CzkToEurVysledek, CzkHodnota,CzkToGbpKurz,CzkToGbpVysledek;

                CzkHodnota = inputValue * 1;
                czk.setText("CZK: " + dec.format(CzkHodnota));

                CzkToUsdKurz = Double.parseDouble(result[0]);
                CzkToUsdVysledek = inputValue * CzkToUsdKurz;
                usd.setText("USD: " + dec.format(CzkToUsdVysledek));

                CzkToEurKurz = Double.parseDouble(result[1]);
                CzkToEurVysledek = inputValue * CzkToEurKurz;
                eur.setText("EUR: " + dec.format(CzkToEurVysledek));

                CzkToGbpKurz = Double.parseDouble(result[2]);
                CzkToGbpVysledek = inputValue * CzkToGbpKurz;
                gbp.setText("GBP: " + dec.format(CzkToGbpVysledek));

            }else if (index == 3) {
                double GbpToUsdKurz, GbpToEurKurz, GbpToUsdVysledek, GbpToEurVysledek,GbpHodnota,GbpToCzkKurz,gbpToCzkVysledek;

                GbpHodnota = inputValue * 1;
                gbp.setText("GBP: " + dec.format(GbpHodnota));

                GbpToUsdKurz = Double.parseDouble(result[0]);
                GbpToUsdVysledek = inputValue * GbpToUsdKurz;
                usd.setText("USD: " + dec.format(GbpToUsdVysledek));

                GbpToEurKurz = Double.parseDouble(result[1]);
                GbpToEurVysledek = inputValue * GbpToEurKurz;
                eur.setText("EUR: " + dec.format(GbpToEurVysledek));

                GbpToCzkKurz = Double.parseDouble(result[2]);
                gbpToCzkVysledek = inputValue * GbpToCzkKurz;
                czk.setText("CZK: " + dec.format(gbpToCzkVysledek));
            }
        }

        @Override
        protected String[] doInBackground(String... strings) {
            if(ulozeno==false){
                if (index == 0) {
                    String url;
                    try {
                        url = getJson("https://api.exchangeratesapi.io/latest?base=USD");
                        //parsovani JSON
                        JSONObject reader;
                        reader = new JSONObject(url);
                        //JSONArray rateArray = reader.getJSONArray("rates");
                        JSONObject pole = reader.getJSONObject("rates");

                        result[0] = pole.getString("EUR");
                        result[1] = pole.getString("CZK");
                        result[2] = pole.getString("GBP");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (index == 1) {
                    String url;
                    try {
                        url = getJson("https://api.exchangeratesapi.io/latest?base=EUR");
                        //parsovani JSON
                        JSONObject reader;
                        reader = new JSONObject(url);
                        JSONObject pole = reader.getJSONObject("rates");

                        result[0] = pole.getString("USD");
                        result[1] = pole.getString("CZK");
                        result[2] = pole.getString("GBP");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (index == 2) {
                    String url;
                    try {
                        url = getJson("https://api.exchangeratesapi.io/latest?base=CZK");
                        //parsovani JSON
                        JSONObject reader;
                        reader = new JSONObject(url);
                        JSONObject pole = reader.getJSONObject("rates");

                        result[0] = pole.getString("USD");
                        result[1] = pole.getString("EUR");
                        result[2] = pole.getString("GBP");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (index == 3) {
                    String url;
                    try {
                        url = getJson("https://api.exchangeratesapi.io/latest?base=GBP");
                        //parsovani JSON
                        JSONObject reader;
                        reader = new JSONObject(url);
                        JSONObject pole = reader.getJSONObject("rates");

                        result[0] = pole.getString("USD");
                        result[1] = pole.getString("EUR");
                        result[2] = pole.getString("CZK");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                nEditor.putString(getString(R.string.volba), String.valueOf(index));
                nEditor.putString(getString(R.string.hodnota), String.valueOf(inputValue));
                nEditor.putString(getString(R.string.vec1), result[0]);
                nEditor.putString(getString(R.string.vec2), result[1]);
                nEditor.putString(getString(R.string.vec3), result[2]);
                ulozeno=true;
                nEditor.putString(getString(R.string.kontrola), "provedeno");
                nEditor.commit();
            }
            else{
                RetrieveSharedPreferences();
            }
            return result;
        }
    }

    public class SpinnerActivity extends Main2Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            parent.getItemAtPosition(pos);
            spinner.setOnItemSelectedListener(this);
            //spinner.setOnItemSelectedListener(this);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    private void decimalBin(String vyber)
    {
        try {
               cislo = Integer.parseInt(editTextDec.getText().toString());
        }
        catch (NumberFormatException e){
            vystup1.setText("Wrong input");
            cislo=0;
        }
        if(cislo != 0) {
            if(vyber.equals("toBin")) {
                bin = Integer.toBinaryString(cislo);
            }else {
                try {
                    bin = Integer.toString(Integer.parseInt(editTextDec.getText().toString(), 2));
                } catch(NumberFormatException e){
                    vystup1.setText("Není binární číslo!");
                    return;
                }
            }
            vystup1.setText(bin);
        }
    }

    private void decimalHex(String vyber)
    {
        if (vyber.equals("toHex")) {
            try {
                cislo2 = Integer.parseInt(editText2.getText().toString());
                bin = Integer.toHexString(cislo2);
                vystup2.setText(bin);
            } catch (NumberFormatException e) {
                vystup2.setText("Not a Decimal number");
            }
        } else {
            try {
                cislo2 = Integer.parseInt(editText2.getText().toString(),16);
                bin = Integer.toString(cislo2);
                vystup2.setText(bin);
            } catch (NumberFormatException e) {
                vystup2.setText("Not a Hexadecimal number");
            }
        }
    }

    private void HexBin(String vyber)
    {
        if (vyber.equals("toHex")) {
            try {
                cislo3 = Integer.parseInt(editText3.getText().toString(),2);
                bin = Integer.toHexString(cislo3);
                vystup3.setText(bin);
            } catch (NumberFormatException e) {
                vystup3.setText("Not a Binary number");
            }
        } else {
            try {
                cislo3 = Integer.parseInt(editText3.getText().toString(),16);
                bin = Integer.toBinaryString(cislo3);
                vystup3.setText(bin);
            } catch (NumberFormatException e) {
                vystup3.setText("Not a Hexadecimal number");
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private final class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
                            finish();
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.slide_in_right,
                                    R.anim.slide_out_left);
                        } else {
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        //onSwipeBottom();
                    } else {
                        //onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

    }
}
