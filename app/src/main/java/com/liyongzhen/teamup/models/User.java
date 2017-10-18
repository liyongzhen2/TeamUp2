package com.liyongzhen.teamup.models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    private String uId = "";
    private String firstname = "";
    private String lastname = "";
    private String email = "";
    private int gender =0;
    private String dateofBirth = "";
    private int experienceLevel = 0;
    private int chooseSport = 0;
    private String photoUrl = "";
    private boolean experience1;
    private boolean experience2;
    private boolean experience3;
    private boolean experience4;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String firstname, String lastname, int gender,
                String dateofBirth, int experienceLevel,
                int chooseSport, String photoUrl,
            boolean experience1, boolean experience2, boolean experience3,
                boolean experience4
                ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.dateofBirth = dateofBirth;
        this.experienceLevel = experienceLevel;
        this.chooseSport = chooseSport;
        this.photoUrl = photoUrl;
        this.experience1 = experience1;
        this.experience2 = experience2;
        this.experience3 = experience3;
        this.experience4 = experience4;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public String getDateofBirth(){
        return dateofBirth;
    }

    public int getGender(){
        return gender;
    }

    public boolean getExperience1(){
        return experience1;
    }

    public boolean getExperience2(){
        return experience2;
    }

    public boolean getExperience3(){
        return experience3;
    }

    public boolean getExperience4(){
        return experience4;
    }

    public int getChooseSport(){
        return chooseSport;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return firstname+" "+lastname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

//    // [START post_to_map]
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("firstname", username);
//        result.put("gender", gender);
//        result.put("dateofBirth", dateofBirth);
//        result.put("experienceLevel", experienceLevel);
//        result.put("chooseSport",chooseSport);
//        return result;
//    }
}
// [END blog_user_class]
