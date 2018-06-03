package com.voteandeat.voteandeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.GoogleAPI.MapsActivity;
import com.voteandeat.voteandeat.Room.Chat.DiscussionActivity;
import com.voteandeat.voteandeat.Room.Model.VotePlace;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatabaseReference dbReferenceMembersCount;
    ListView listViewPlacesLastStep;
    TextView tvMembersRoom;
    ImageButton ibShowMemeberRoom;
    List<VotePlace> votePlaces;

    String idRoom;
    Button btnChatRoom, btnAddRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        tvMembersRoom = findViewById(R.id.membersRoom);
        ibShowMemeberRoom = findViewById(R.id.ibShowMemeberRoom);

        idRoom = getIntent().getExtras().get("idActualRoom").toString();

        //Count Members
        dbReferenceMembersCount = FirebaseDatabase.getInstance().getReference("Rooms").child(idRoom).child("Members");
        dbReferenceMembersCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvMembersRoom.setText(dataSnapshot.getChildrenCount()+" Members");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ibShowMemeberRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(RoomActivity.this, MembersInRoomActivity.class);
                i2.putExtra("idActualRoom", idRoom);
                startActivity(i2);
            }
        });

        btnChatRoom = findViewById(R.id.btnChatRoom);
        btnAddRestaurant = findViewById(R.id.btnAddRestaurantRoom);

        btnChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RoomActivity.this, DiscussionActivity.class);
                i.putExtra("idActualRoom", idRoom);
                startActivity(i);
            }
        });

        btnAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RoomActivity.this, MapsActivity.class);
                i.putExtra("idActualRoom", idRoom);
                startActivity(i);
            }
        });


        votePlaces = new ArrayList<VotePlace>();
        listViewPlacesLastStep = findViewById(R.id.lvListOfPlacesLastStep);

        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms").child(idRoom).child("Votes").child("VotePlace");
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                votePlaces.clear();

                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    VotePlace votePlace = postSnapShot.getValue(VotePlace.class);
                    votePlaces.add(votePlace);
                }

                VotePlaceList votePlaceAdapter = new VotePlaceList(RoomActivity.this, votePlaces,databaseReference);
                listViewPlacesLastStep.setAdapter(votePlaceAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}