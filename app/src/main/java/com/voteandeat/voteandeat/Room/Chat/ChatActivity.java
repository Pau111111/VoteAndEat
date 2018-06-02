package com.voteandeat.voteandeat.Room.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    ListView lvDiscussionTopics;
    ArrayList<String> listOfChats = new ArrayList<String>();
    ArrayAdapter arrayChats;

    String UserName;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        lvDiscussionTopics = (ListView) findViewById(R.id.lvDiscussionTopics);
        arrayChats = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOfChats);
        lvDiscussionTopics.setAdapter(arrayChats);


        //getUserName();

        dbr = FirebaseDatabase.getInstance().getReference("Rooms");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
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

            }
        });



        lvDiscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
                i.putExtra("idActualRoom", ((TextView)view).getText().toString());
                i.putExtra("user_name", UserName);
                startActivity(i);
            }
        });
    }
}
