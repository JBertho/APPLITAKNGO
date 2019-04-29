package fr.takngo.application.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

        private int id;
        private String email;
        private String lastname;
        private String name;
        private String birthday;
        private String password;
        private String profile_pict;
        private int company;
        private String address;
        private String postal_code;
        private String phone;





        public static User UserFromArray(JSONObject data) throws JSONException {

            User u = new User(-1,
                    data.getString("email"),
                    data.getString("lastname"),
                    data.getString("name"),
                    data.getString("birthday"),
                    data.getString("password"),
                    data.getString("profile_pict"),
                    data.getInt("company_id"),
                    data.getString("address"),
                    data.getString("postal_code"),
                    data.getString("phone")
            );
            if(data.getInt("id") != -1){
                u.setId(data.getInt("id"));
            }
            return u;

    }

        private User(int id,
                     String email,
                     String lastname,
                     String name,
                     String birthday,
                     String password,
                     String profile_pict,
                     int company,
                     String address,
                     String postal_code,
                     String phone)
        {
            this.id = id;
            this.email = email;
            this.lastname = lastname;
            this.name = name;
            this.birthday = birthday;
            this.password = password;
            this.profile_pict = profile_pict;
            this.company = company;
            this.address = address;
            this.postal_code = postal_code;
            this.phone = phone;
        }

        private void setId(int id){
            this.id = id;
        }

}

