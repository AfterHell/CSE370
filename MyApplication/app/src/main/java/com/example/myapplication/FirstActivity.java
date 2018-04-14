package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class FirstActivity extends AppCompatActivity implements SensorEventListener
{
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senLinearAcceleration;


    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //get sensor manager
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //1. get acceleration with gravity
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        //2. get acceleration without gravity
        senLinearAcceleration = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senSensorManager.registerListener(this, senLinearAcceleration , SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Unregister the sensor when it's on pause
    protected void onPause()
    {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    //Register the sensor when it's resumed
    protected void onResume()
    {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senLinearAcceleration , SensorManager.SENSOR_DELAY_NORMAL);
    }


    //Here we can detect the data returned from the sensor whenever it's changed
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor mySensor = sensorEvent.sensor;

        //1. accelerometer
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            printPosture(x, y, z);
            //get the data every 100 miliseconds
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                //get the speed the shake gesture
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD)
                {
                    getRandomNumber();;
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

        //2. linear acceleration
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            printActivity(x, y, z);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    private void printPosture(float x, float y, float z)
    {
        TextView text = (TextView)findViewById(R.id.editText2);
        text.setText("x = "+ x+ " y = "+ y + " z = "+z);

        TextView postureTextView = (TextView)findViewById(R.id.editText3);
        if(Math.abs(z) <= 4)
        {
            postureTextView.setText("Vertical Position");
        }
        else if(z < 0)
        {
            postureTextView.setText("Facing Down");
        }
        else
        {
            postureTextView.setText("Facing Up");
        }
    }

    private void printActivity(float x, float y, float z)
    {
        TextView activityTextView = (TextView)findViewById(R.id.editText4);
        //activityTextView.setText("x = " + x + " y = " + y + " z = " + z);
        double acceleration = Math.sqrt(x*x+y*z+z*z);
        if(acceleration <= 0.1)
        {
            activityTextView.setText("Stopped: "+acceleration);
        }
        else if(acceleration <= 5)
        {
            activityTextView.setText("Walking: "+acceleration);
        }
        else if(acceleration <= 30)
        {
            activityTextView.setText("Running: "+acceleration);
        }
        else if(acceleration > 30)
        {
            activityTextView.setText("In a car: "+acceleration);
        }
        else
        {
            activityTextView.setText("Stopped: "+acceleration);
        }
    }

    private void getRandomNumber()
    {
        ArrayList numbersGenerated = new ArrayList();

        for (int i = 0; i < 6; i++)
        {
            Random randNumber = new Random();
            int iNumber = randNumber.nextInt(48) + 1;

            if(!numbersGenerated.contains(iNumber))
            {
                numbersGenerated.add(iNumber);
            }
            else
            {
                i--;
            }
        }

        TextView text = (TextView)findViewById(R.id.number_1);
        text.setText(""+numbersGenerated.get(0));

        text = (TextView)findViewById(R.id.number_2);
        text.setText(""+numbersGenerated.get(1));

        text = (TextView)findViewById(R.id.number_3);
        text.setText(""+numbersGenerated.get(2));

        text = (TextView)findViewById(R.id.number_4);
        text.setText(""+numbersGenerated.get(3));

        text = (TextView)findViewById(R.id.number_5);
        text.setText(""+numbersGenerated.get(4));

        text = (TextView)findViewById(R.id.number_6);
        text.setText(""+numbersGenerated.get(5));

        FrameLayout ball1 = (FrameLayout) findViewById(R.id.ball_1);
        ball1.setVisibility(View.INVISIBLE);

        FrameLayout ball2 = (FrameLayout) findViewById(R.id.ball_2);
        ball2.setVisibility(View.INVISIBLE);

        FrameLayout ball3 = (FrameLayout) findViewById(R.id.ball_3);
        ball3.setVisibility(View.INVISIBLE);

        FrameLayout ball4 = (FrameLayout) findViewById(R.id.ball_4);
        ball4.setVisibility(View.INVISIBLE);

        FrameLayout ball5 = (FrameLayout) findViewById(R.id.ball_5);
        ball5.setVisibility(View.INVISIBLE);

        FrameLayout ball6 = (FrameLayout) findViewById(R.id.ball_6);
        ball6.setVisibility(View.INVISIBLE);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.move_down_ball_first);
        ball6.setVisibility(View.VISIBLE);
        ball6.clearAnimation();
        ball6.startAnimation(a);

        ball5.setVisibility(View.VISIBLE);
        ball5.clearAnimation();
        ball5.startAnimation(a);

        ball4.setVisibility(View.VISIBLE);
        ball4.clearAnimation();
        ball4.startAnimation(a);

        ball3.setVisibility(View.VISIBLE);
        ball3.clearAnimation();
        ball3.startAnimation(a);

        ball2.setVisibility(View.VISIBLE);
        ball2.clearAnimation();
        ball2.startAnimation(a);

        ball1.setVisibility(View.VISIBLE);
        ball1.clearAnimation();
        ball1.startAnimation(a);
    }

}
