package com.voteandeat.voteandeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.VotePlace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RoomActivity extends AppCompatActivity {
    ListView lvPlaces;
    ArrayList<String> listOfPlaces = new ArrayList<String>();
    ArrayAdapter arrayPlaces;
    String idRoom;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        lvPlaces = findViewById(R.id.lvListOfPlacesLastStep);
        arrayPlaces = new ArrayAdapter(this,R.layout.simple_place_item,R.id.voteNameLastStep,listOfPlaces);
        lvPlaces.setAdapter(arrayPlaces);

        idRoom = getIntent().getExtras().get("selected_topic").toString();

        dbr = FirebaseDatabase.getInstance().getReference("Rooms").child(idRoom).child("Votes").child("VotePlace");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> placeList = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    VotePlace place = postSnapshot.getValue(VotePlace.class);
                    Log.d("Place: " , String.valueOf(place));
                    placeList.add(String.valueOf(place.getName()));
                }
                arrayPlaces.clear();
                arrayPlaces.addAll(placeList);
                arrayPlaces.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
    });

}
}