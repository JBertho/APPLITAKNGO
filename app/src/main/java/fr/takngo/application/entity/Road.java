package fr.takngo.application.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Road implements Parcelable {
    private int id;
    private int driver;
    private String start_street;
    private String end_start;
    private float distance;
    private String appointment;
    private int order;
    private float price;

    public Road(int id, int driver, String start_street, String end_start, float distance, String appointment, int order,float price) {
        this.id = id;
        this.driver = driver;
        this.start_street = start_street;
        this.end_start = end_start;
        this.distance = distance;
        this.appointment = appointment;
        this.order = order;
        this.price = price;
    }

    protected Road(Parcel in) {
        id = in.readInt();
        driver = in.readInt();
        start_street = in.readString();
        end_start = in.readString();
        distance = in.readFloat();
        appointment = in.readString();
        order = in.readInt();
        price = in.readFloat();
    }

    public static final Creator<Road> CREATOR = new Creator<Road>() {
        @Override
        public Road createFromParcel(Parcel in) {
            return new Road(in);
        }

        @Override
        public Road[] newArray(int size) {
            return new Road[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getDriver() {
        return driver;
    }

    public String getStart_street() {
        return start_street;
    }

    public String getEnd_start() {
        return end_start;
    }

    public float getDistance() {
        return distance;
    }

    public String getAppointment() {
        return appointment;
    }

    public int getOrder() {
        return order;
    }

    public float getPrice() {
        return price;
    }

    public static Road RoadFromJson(JSONObject object) throws JSONException {
        Log.d("road",object.has("order_id")+"");
        int order = -1;
        if (object.has("order_id")){
            order = object.getInt("order_id");
        }


        Road road = new Road(
                object.getInt("id"),
                -1,
                object.getString("start_street"),
                object.getString("end_street"),
                object.getLong("distance"),
                object.getString("appointment"),
                order,
                object.getLong("price")

        );
        return road;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(driver);
        parcel.writeString(start_street);
        parcel.writeString(end_start);
        parcel.writeFloat(distance);
        parcel.writeString(appointment);
        parcel.writeInt(order);
        parcel.writeFloat(price);
    }
}
