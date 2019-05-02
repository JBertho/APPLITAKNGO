package fr.takngo.application.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Parcelable {

    private int id;
    private String name;
    private float price;
    private String description;
    private int service;
    private String  pict;
    private String duration_activity;

    public Product(int id, String name, float price, String description, int service, String pict, String duration_activity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.service = service;
        this.pict = pict;
        this.duration_activity = duration_activity;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readFloat();
        description = in.readString();
        service = in.readInt();
        pict = in.readString();
        duration_activity = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getService() {
        return service;
    }

    public String getPict() {
        return pict;
    }

    public String getDuration_activity() {
        return duration_activity;
    }

    public static Product ProductFromJSON(JSONObject datas) throws JSONException {

        String duration = "null";
        if (datas.has("duration_activity")){
            duration = datas.getString("duration_activity");
        }


        Product product = new Product(
                datas.getInt("id"),
                datas.getString("name"),
                (float)datas.getDouble("price"),
                datas.getString("description"),
                datas.getInt("service_id"),
                datas.getString("pict"),
                duration
        );
        return product;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeFloat(price);
        parcel.writeString(description);
        parcel.writeInt(service);
        parcel.writeString(pict);
        parcel.writeString(duration_activity);
    }
}
