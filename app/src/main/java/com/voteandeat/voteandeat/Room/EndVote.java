package com.voteandeat.voteandeat.Room;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.R;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.RoomActivity;

public class EndVote extends AppCompatActivity {
    DatabaseReference databaseReference;

    String idRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_endvote_item);

        idRoom = getIntent().getExtras().get("idActualRoom").toString();

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*databaseReference.addValueEventListener(new ValueEventListener() {
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
        });*/

    }
}
