package com.voteandeat.voteandeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.voteandeat.voteandeat.Room.MemberList;
import com.voteandeat.voteandeat.Room.Model.Member;

import java.util.ArrayList;
import java.util.List;

public class MembersInRoomActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    List<Member> members;
    ListView lvMembersListRoom;
    String idRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_in_room);

    }

    @Override
    protected void onStart() {
        super.onStart();

        idRoom = getIntent().getExtras().get("idActualRoom").toString();

        members = new ArrayList<Member>();

        lvMembersListRoom = findViewById(R.id.lvMembersListRoom);

        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms").child(idRoom).child("Members");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                members.clear();

                for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
                    Member member = postSnapShot.getValue(Member.class);
                    members.add(member);
                }

                MemberList memberListAdapter = new MemberList(MembersInRoomActivity.this, members,databaseReference);
                lvMembersListRoom.setAdapter(memberListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
