package com.example.student.lifecyclesdemo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class Main2Activity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;
    //final float amountToMoveRight=200;
    final float amountToMoveDown=100;
    final float amountToMoveUp =100;

    ViewGroup _root;
    private int _xDelta;
    private int _yDelta;

    Button prevod,prevod2,prevod3;
    EditText editTextDec,editText2,editText3;
    TextView vystup1,vystup2,vystup3;
    Spinner spinner,spinner2,spinner3;
    ArrayAdapter<CharSequence> adapter,adapter2,adapter3;
    LinearLayout tabulka;

    private int cislo,cislo2,cislo3;
    private String bin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        gestureObject = new GestureDetectorCompat(this,new LearnGesture());

       prevod = (Button) findViewById(R.id.button19);
       prevod2 = (Button) findViewById(R.id.button20);
       prevod3 = (Button) findViewById(R.id.button21);

       editTextDec= (EditText) findViewById(R.id.editText5);
       editText2= (EditText) findViewById(R.id.editText2);
       editText3= (EditText) findViewById(R.id.editText3);

       vystup1 = (TextView) findViewById(R.id.textView);
       vystup2 = (TextView) findViewById(R.id.textView3);
       vystup3 = (TextView) findViewById(R.id.textView6);

       spinner = (Spinner) findViewById(R.id.spinner);
       spinner2 = (Spinner) findViewById(R.id.spinner2);
       spinner3 = (Spinner) findViewById(R.id.spinner3);

       tabulka = (LinearLayout) findViewById(R.id.linearLayout2);

       adapter = ArrayAdapter.createFromResource(this,
                R.array.typy_prevodu, android.R.layout.simple_spinner_item);

       adapter2 = ArrayAdapter.createFromResource(this,
                R.array.prevodHex, android.R.layout.simple_spinner_item);

       adapter3 = ArrayAdapter.createFromResource(this,
                R.array.prevodBinHex, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

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

        /*TranslateAnimation anim = new TranslateAnimation(0, amountToMoveRight, 0, amountToMoveDown);
        anim.setDuration(1000);
        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)tabulka.getLayoutParams();
                params.topMargin += amountToMoveDown;
                params.leftMargin += amountToMoveRight;
                tabulka.setLayoutParams(params);
            }
        });

        tabulka.startAnimation(anim);*/

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)tabulka.getLayoutParams();
        params.bottomMargin += amountToMoveUp;
        params.topMargin += amountToMoveDown;
        params.leftMargin = params.rightMargin;
        params.rightMargin = params.leftMargin;
        tabulka.setLayoutParams(params);

        tabulka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ukazalo message
            }
        });

        // Implement it's on touch listener.
        tabulka.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        ConstraintLayout.LayoutParams lParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.bottomMargin;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                        //layoutParams.leftMargin = X -_xDelta;
                        layoutParams.topMargin = -500;
                        layoutParams.rightMargin = -500;
                        layoutParams.bottomMargin = Y -_yDelta;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                //_root.invalidate();
                return true;
            }
        });


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
