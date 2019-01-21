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
            buttonNasobeni, buttonTecka, buttonClear, buttonVysledek,buttonInv, buttonDropbox;

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
        buttonDropbox = (Button) findViewById(R.id.button27);

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

        buttonDropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Main3Activity.class);
                finish();
                startActivity(myIntent);
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

    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private final class LearnGesture extends GestureDetector.SimpleOnGestureListener{

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

                        } else {
                            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                            finish();
                            startActivity(myIntent);
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
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
