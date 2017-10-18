package com.liyongzhen.teamup.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liyongzhen.teamup.R;
import com.liyongzhen.teamup.models.User;
import com.liyongzhen.teamup.utils.CommonValuables;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

//    private DrawerLayout mDrawerLayout;
    private GoogleMap mMap;

    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Team Up");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initControllers();

//        textView = (TextView) findViewById(R.id.textView2);
//        initFirebaseDatabase();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_map_setting:
                gotoMapSettingPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initControllers(){
//        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_maps_drawerlayout);
//        findViewById(R.id.menu_button).setOnClickListener(this);
//        findViewById(R.id.menu_teamup).setOnClickListener(this);
//        findViewById(R.id.menu_timeline).setOnClickListener(this);
//        findViewById(R.id.menu_profile).setOnClickListener(this);
//        findViewById(R.id.menu_signout).setOnClickListener(this);
//        findViewById(R.id.menu_button_sub).setOnClickListener(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void gotoProfilePage(){
        Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void initFirebaseDatabase(){
        if(!checkFirebaseAuth()) return;
        // Write a message to the database
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            Log.d(CommonValuables.TAG, "user nill");
                            gotoProfilePage();
                        } else {
                            Log.d(CommonValuables.TAG, user.toString());
                            // Write new post
//                            updataMenu(user.username, user.photo);
//                            appUser = user;
//                            appUserId = userId;
//                            statusOfFragment = 4;
//                            replaceFragments(mHomeFragment);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(CommonValuables.TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
//        myRef.child("message").setValue("Hello, World!");
//        // Read from the database
//        myRef.child("message").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(CommonValuables.TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(CommonValuables.TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

    private boolean checkFirebaseAuth(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
//            // Name, email address, and profile photo Url
//            String name = firebaseUser.getDisplayName();
//            String email = firebaseUser.getEmail();
//            if(email != null)
//                textView.setText(email);
//            Uri photoUrl = firebaseUser.getPhotoUrl();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = firebaseUser.getUid();
            return true;
        }
        else{
//            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
            return false;
        }

    }

    private void gotoMapSettingPage(){

            Intent intent = new Intent(MapActivity.this, MapSettingActivity.class);
            startActivity(intent);
    }

    @Override
    public void onClick(View view) {
//        if(view.getId() == R.id.menu_button){
//            mDrawerLayout.openDrawer(GravityCompat.START);
//        }
//        else if(view.getId() == R.id.menu_teamup){
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        }
//        else if(view.getId() == R.id.menu_timeline){
//            Intent intent = new Intent(MapActivity.this, TimeLineActivity.class);
//            startActivity(intent);
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//            finish();
//        }
//        else if(view.getId() == R.id.menu_profile){
//            Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
//            startActivity(intent);
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        }
//        else if(view.getId() == R.id.menu_signout){
//
//        }
//        else if(view.getId() == R.id.menu_button_sub){
//        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
