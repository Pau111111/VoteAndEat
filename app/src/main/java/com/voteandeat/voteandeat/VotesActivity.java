package com.voteandeat.voteandeat;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class VotesActivity extends Fragment {
    ListView lvDiscussionTopics;
    ArrayList<String> listOfChats = new ArrayList<String>();
    ArrayAdapter arrayChats;
    ArrayList<String> listOfIdChats = new ArrayList<String>();

    String UserName;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_votes,container,false);

        lvDiscussionTopics = view.findViewById(R.id.lvVoteRooms);
        arrayChats = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1, listOfChats);
        lvDiscussionTopics.setAdapter(arrayChats);

        //getUserName();

        dbr = FirebaseDatabase.getInstance().getReference("Rooms");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> roomList = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Room room = postSnapshot.getValue(Room.class);
                    roomList.add(String.valueOf(room.getName()));
                    listOfIdChats.add(room.getId());
                }
                arrayChats.clear();
                arrayChats.addAll(roomList);
                arrayChats.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
           /* @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayChats.clear();
                arrayChats.addAll(set);
                arrayChats  .notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }*/
        });

        lvDiscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), RoomActivity.class);
                i.putExtra("idActualRoom", (listOfIdChats.get(position)));
                i.putExtra("room_name", ((TextView)view).getText().toString());
                i.putExtra("user_name", UserName);
                Log.d("Position: " , String.valueOf(position));
                startActivity(i);
            }
        });
        return view;
    }
}
