package com.swash.kencommerce.LocationUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLogTransfar implements Parcelable {  
    private double lat;  
    private double log;  
    private int action;  

    public double getLat() {  
 return lat;  
    }  
    public void setLat(double lat) {  
 this.lat = lat;  
    }  
    public double getLog() {  
 return log;  
    }  
    public void setLog(double log) {  
 this.log = log;  
    }  
    public int getAction() {  
 return action;  
    }  
    public void setAction(int action) {  
 this.action = action;  
    }  

    public static final Creator<LatLogTransfar> CREATOR = new Creator<LatLogTransfar>() {
    public LatLogTransfar createFromParcel(Parcel source) {  
     LatLogTransfar mArg = new LatLogTransfar();  
     mArg.lat = source.readDouble();  
     mArg.log = source.readDouble();  
     mArg.action = source.readInt();  
     return mArg;  
 }  
 public LatLogTransfar[] newArray(int size) {  
     return new LatLogTransfar[size];  
 }  
    };  

    public int describeContents() {  
 return 0;  
    }  
    public void writeToParcel(Parcel parcel, int flags) {  
 parcel.writeDouble(lat);  
 parcel.writeDouble(log);  
 parcel.writeInt(action);  
    }  
}
