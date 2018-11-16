package com.example.student.lifecyclesdemo;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private double num1=Double.NaN;
    private double num2=Double.NaN;
    private double mezivypocet=Double.NaN;
    private double pom;

    DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    Button button0, button1, button2, button3, button4, button5, button6,
            button7, button8, button9, buttonPlus, buttonMinus, buttonDeleni,
            buttonNasobeni, buttonTecka, buttonClear, buttonVysledek,buttonInv, next;

    EditText myEditText;
    TextView zobraz;
    int position;

    private static final char scitani = '+';
    private static final char rozdil = '-';
    private static final char nasobeni = '*';
    private static final char deleni = '/';
    private char akce;

    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast toast = Toast.makeText(this, "onCreate Triggered", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("LIFECYCLE", "OnCreate Triggered");

        button0 = (Button) findViewById(R.id.button16);
        button1 = (Button) findViewById(R.id.button2);
        button2 = (Button) findViewById(R.id.button3);
        button3 = (Button) findViewById(R.id.button4);
        button4 = (Button) findViewById(R.id.button8);
        button5 = (Button) findViewById(R.id.button6);
        button6 = (Button) findViewById(R.id.button7);
        button7 = (Button) findViewById(R.id.button10);
        button8 = (Button) findViewById(R.id.button11);
        button9 = (Button) findViewById(R.id.button12);
        buttonTecka = (Button) findViewById(R.id.button15);
        buttonPlus = (Button) findViewById(R.id.button5);
        buttonMinus = (Button) findViewById(R.id.button9);
        buttonNasobeni = (Button) findViewById(R.id.button13);
        buttonDeleni = (Button) findViewById(R.id.button14);
        buttonClear = (Button) findViewById(R.id.button);
        buttonVysledek = (Button) findViewById(R.id.button17);
        buttonInv = (Button) findViewById(R.id.button18);
        myEditText = (EditText) findViewById(R.id.edt1);
        zobraz = (TextView) findViewById(R.id.display);
       //next = (Button) findViewById(R.id.Button01);

        /*next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), Activity2.class);
                //startActivityForResult(myIntent, 0);
            }

        });*/
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "1");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "2");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "3");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "4");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "5");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "6");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "7");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "8");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "9");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        buttonTecka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + ".");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText(myEditText.getText() + "0");
                position = myEditText.length();
                myEditText.setSelection(position);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEditText.setText("");
                zobraz.setText("");
                mezivypocet = Double.NaN;
                num1 = Double.NaN;
                num2 = Double.NaN;
                akce = '0';
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kalkulacka();
                akce = scitani;
                if (!Double.isNaN(num1)) {
                    zobraz.setText(decimalFormat.format(num1) + "+");
                } else {
                    zobraz.setText("Neni zadan vstup");
                }
                myEditText.setText(null);
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*myEditText.setText(myEditText.getText() + "-");
                position = myEditText.length();
                myEditText.setSelection(position);*/

                kalkulacka();
                akce = rozdil;
                if (!Double.isNaN(num1)) {
                    zobraz.setText(decimalFormat.format(num1) + "-");
                } else {
                    zobraz.setText("Neni zadan vstup");
                }
                myEditText.setText(null);
            }
        });

        buttonInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!myEditText.getText().toString().matches("")) {
                    pom = Double.parseDouble(myEditText.getText().toString());
                    pom = pom * (-1);
                    myEditText.setText(decimalFormat.format(pom));
                    position = myEditText.length();
                    myEditText.setSelection(position);
                }
            }
        });

        buttonNasobeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kalkulacka();
                akce = nasobeni;
                if (!Double.isNaN(num1)) {
                    zobraz.setText(decimalFormat.format(num1) + "*");
                } else {
                    zobraz.setText("Neni zadan vstup");
                }
                myEditText.setText(null);
            }
        });

        buttonDeleni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kalkulacka();
                akce = deleni;
                if (!Double.isNaN(num1)) {
                    zobraz.setText(decimalFormat.format(num1) + "/");
                } else {
                    zobraz.setText("Neni zadan vstup");
                }
                myEditText.setText(null);
            }
        });

        buttonVysledek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kalkulacka();
                if (!Double.isNaN(num1) && !Double.isNaN(num2)) {
                    zobraz.setText(zobraz.getText().toString() +
                            decimalFormat.format(num2) + " = " + decimalFormat.format(num1));
                } else if (!Double.isNaN(num1) && Double.isNaN(num2)) {
                    zobraz.setText(decimalFormat.format(num1));
                } else {
                    zobraz.setText("");
                }
                mezivypocet = num1;
                num1 = Double.NaN;
                akce = '0';
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event){
      this.gestureObject.onTouchEvent(event);
      return super.onTouchEvent(event);
    }


    private void kalkulacka() {
        //kontrola jestli je cislo
        if(!Double.isNaN(num1)) {
            try {
                num2 = Double.parseDouble(myEditText.getText().toString());
            }catch (Exception e){
                zobraz.setText("Syntax Error");
                num2 = Double.NaN;
            }
            myEditText.setText(null);

            if(!Double.isNaN(num2)) {
                if (akce == scitani)
                    num1 = this.num1 + num2;
                else if (akce == rozdil) {
                    if(num2<0)
                    {
                        num1 = this.num1 + (num2*(-1));
                    }else
                    num1 = this.num1 - num2;
                    }
                else if (akce == nasobeni)
                    num1 = this.num1 * num2;
                else if (akce == deleni)
                    num1 = this.num1 / num2;
            }
        }
        else {
            try {
                if(Double.isNaN(num1) && !myEditText.getText().toString().matches(""))
                {
                    num1 = Double.parseDouble(myEditText.getText().toString());
                }else
                num1=mezivypocet;
            }
            catch (Exception e){
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast toast = Toast.makeText(this, "onStart Triggered", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("LIFECYCLE", "OnStart Triggered");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast toast = Toast.makeText(this, "onResume Triggered", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("LIFECYCLE", "OnResume Triggered");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("LIFECYCLE", "OnPause Triggered");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("LIFECYCLE", "OnStop Triggered");
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        public boolean onFling(MotionEvent event1,MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()> event1.getY()){

                Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                finish();
                startActivity(myIntent);
            }
            else
            if(event2.getX()< event1.getY()){

            }
            return true;

        }


    }
}
