/**
 * @author: Sinan Alkaya
 * date of current build: 07.02.2019 (dd-mm-yyyy)
 */
package gforcemeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.meter.R;
import com.orm.SugarDb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager accel;
    private static final String TAG = "main";
    public static double gravityC  = 9.81;
    private double highest = 1;
    private double lowest = 1;
    private TextView maintxt;
    private TextView minG;
    private TextView maxG;
    private gforce gf;
    private gforceDB gdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //i know not the greatest software design
        //this.saveData();
        this.getData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accel = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel.registerListener(this, accel.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        maintxt = (TextView)findViewById(R.id.gload);

        this.minG = (TextView)findViewById(R.id.minG);
        this.maxG = (TextView)findViewById(R.id.maxG);

        final Button resetbtn = (Button)findViewById(R.id.resetbtn);
        this.gf = new gforce(this);
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gf.reset();
            }
        });

        final Button savebtn = (Button)findViewById(R.id.savebtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gdb = new gforceDB(highest, lowest);
                saveData();
            }
        });
    }

    public double getGforce(double x, double y, double z){
        double result = 0.0f;
        result = Math.sqrt(x * x + y*y + z*z);
        return result / gravityC;
    }

    public void setHighest(double value){
        this.highest = value;
        this.maxG.setText(new DecimalFormat("##.##").format(this.highest) + " g");
    }

    public void setLowest(double value){
        this.lowest = value;
        this.minG.setText(new DecimalFormat("##.##").format(this.lowest) + " g");
    }

    public void reset(){
        this.setLowest(1.000000000);
        this.setHighest(1.000000000);
    }
    public void saveData(){
        gdb.save();
    }

    public void getData(){
        List<gforceDB> data = gforceDB.find(gforceDB.class, "MAX_VAL > 1");
        Log.d("DATABASE","found Data:"+data.get(0).getMaxVal());
        //get latest:
        Log.d("LENGTH","Length of Array:"+data.size());
        double latestMax = data.get(data.size()-1).getMaxVal();
        double latestMin = data.get(data.size()-1).getMinVal();
        this.highest = latestMax;
        this.lowest = latestMin;
        //setting text throws error :(
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            double x = getGforce(event.values[0], event.values[1], event.values[2]);
            //Log.d(TAG,x + "");
            this.setText(x);
            if(x > this.highest){
                setHighest(x);
            }
            if(x < this.lowest){
                setLowest(x);
            }

        }
    }
    public void setText(double text){
        maintxt.setText(new DecimalFormat("##.##").format(text) + " g");
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //do nothing, not required
    }
}
