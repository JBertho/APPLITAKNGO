package fr.takngo.application.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Service implements Parcelable {

    private int id;
    private String name;
    private String description;
    private int category;
    private int chief;
    private String address;
    private String postal_code;
    private String picture;

    protected Service(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        category = in.readInt();
        chief = in.readInt();
        address = in.readString();
        postal_code = in.readString();
        picture = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public int getChief() {
        return chief;
    }

    public String getAddress() {
        return address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getPicture() {
        return picture;
    }

    public Service(int id, String name, String description, int category, int chief, String address, String postal_code, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.chief = chief;
        this.address = address;
        this.postal_code = postal_code;
        this.picture = picture;
    }

    public static Service ServiceFromArray(JSONObject data) throws JSONException {
        Service service = new Service(
                data.getInt("id"),
                data.getString("name"),
                data.getString("description"),
                data.getInt("category_id"),
                data.getInt("chief_id"),
                data.getString("address"),
                data.getString("postal_code"),
                data.getString("picture")
        );
        return service;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(category);
        parcel.writeInt(chief);
        parcel.writeString(address);
        parcel.writeString(postal_code);
        parcel.writeString(picture);
    }
}
