package gforcemeter;


import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.orm.SugarRecord;

import java.time.LocalDateTime;
import java.util.*;
public class gforceDB extends SugarRecord {
    private double minVal;
    private double maxVal;
    public gforceDB(double min, double max){
        setMinVal(min);
        setMaxVal(max);
    }
    public gforceDB(){
        setMinVal(1);
        setMaxVal(1);
    }

    public double getMinVal() {
        return minVal;
    }
    public void setMinVal(double minVal){
        this.minVal = minVal;
    }

    public double getMaxVal(){
        return this.maxVal;
    }
    public void setMaxVal(double maxVal){
        this.maxVal = maxVal;
    }
}
