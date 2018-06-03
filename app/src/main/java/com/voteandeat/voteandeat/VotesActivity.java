package com.voteandeat.voteandeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.Room.Chat.DiscussionActivity;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.Vote;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.Room.RoomList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VotesActivity extends Fragment {
    DatabaseReference databaseReference;
    ListView listViewRooms;
    List<Room> roomList;
    String idRoom;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_votes, container, false);

        roomList = new ArrayList<Room>();
        listViewRooms = view.findViewById(R.id.lvVoteRooms);

        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomList.clear();

                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    Room room = postSnapShot.getValue(Room.class);
                    roomList.add(room);
                }

                RoomList roomListAdapter = new RoomList(getActivity(), roomList,databaseReference);
                listViewRooms.setAdapter(roomListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

