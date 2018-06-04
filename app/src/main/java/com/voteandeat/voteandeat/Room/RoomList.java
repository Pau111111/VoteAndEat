package com.voteandeat.voteandeat.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.R;
import com.voteandeat.voteandeat.Room.Model.Member;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.RoomActivity;

import java.util.ArrayList;
import java.util.List;

public class RoomList  extends ArrayAdapter<Room> {
    private Activity context;
    private List<Room> roomList;
    ArrayList<Boolean> isUserInRoom;
    DatabaseReference databaseReference;
    DatabaseReference dbReferenceMembers;
    DatabaseReference dbReferenceAllCurrentUser;
    FirebaseAuth mAuth;
    //Boolean memberIsInTheRoom = false;

    String userName;
    String userPhoto;
    String idCurrentUser;

    public RoomList(Activity context, List<Room> roomList, DatabaseReference databaseReference) {
        super(context, R.layout.activity_votes,roomList);
        this.context = context;
        this.roomList = roomList;
        this.databaseReference = databaseReference;
    }
    public View getView(final int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.simple_room_item,null,true);

        isUserInRoom = new ArrayList<Boolean>();

        mAuth = FirebaseAuth.getInstance();

        //Current user
        idCurrentUser = mAuth.getCurrentUser().getUid();
        dbReferenceAllCurrentUser = FirebaseDatabase.getInstance().getReference("Users");
        dbReferenceAllCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child(idCurrentUser).child("name").getValue(String.class);
                userPhoto = dataSnapshot.child(idCurrentUser).child("photourl").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final TextView tvRoomNameList = listViewItem.findViewById(R.id.tvRoomNameList);
        final TextView tvMemebersList = listViewItem.findViewById(R.id.tvMemebersList);

        final Room room = roomList.get(pos);
        tvRoomNameList.setText(room.getName());

        //Count Members
        dbReferenceMembers = FirebaseDatabase.getInstance().getReference("Rooms").child(room.getId()).child("Members");
        dbReferenceMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //isUserInRoom.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Member member = postSnapShot.getValue(Member.class);
                    isUserInRoom.add(false);
                    if (member.getIdUser().equals(idCurrentUser)) {
                        isUserInRoom.add(pos,true);
                    }/*else{
                        if(pos >= isUserInRoom.size()){isUserInRoom.add(pos,false);}
                    }*/
                }
                tvMemebersList.setText(dataSnapshot.getChildrenCount()+" Members");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO if user isn't inside the room

                /*Query query = dbReferenceMembers.orderByChild("idUser").equalTo(idCurrentUser);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id equals to idUser
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                memberIsInTheRoom = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


                //TODO no entra aquí, fer-ho d'una altre manera
               /* dbReferenceMemberIsInTheRoom =  FirebaseDatabase.getInstance().getReference("Rooms").child(room.getId()).child("Members");
                dbReferenceMembers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            Member member = postSnapShot.getValue(Member.class);
                            if (member.getIdUser().equals(idCurrentUser)) {
                                memberIsInTheRoom = true;
                                Log.d("Puchu","memberIsInTheRoom: "+member.getIdUser());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });*/

                if (isUserInRoom.get(pos) != true){
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                    View mView = LayoutInflater.from(context).inflate(R.layout.dialog_room_password, null);
                    mbuilder.setView(mView);
                    final AlertDialog dialog = mbuilder.create();
                    dialog.show();

                final EditText etSubmitedPassword = mView.findViewById(R.id.etSubmitedPassword);
                final Button btnSubmitedPassword = mView.findViewById(R.id.btnSubmitedPassword);

                btnSubmitedPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etSubmitedPassword.getText().toString().equals(room.getPassword())) {
                            Intent i = new Intent(context, RoomActivity.class);
                            i.putExtra("idActualRoom", room.getId());
                            i.putExtra("room_name", room.getName());

                            dbReferenceAllCurrentUser = FirebaseDatabase.getInstance().getReference("Rooms").child(room.getId()).child("Members");
                            String memberNewKey = dbReferenceAllCurrentUser.push().getKey();
                            Member member = new Member(idCurrentUser,userName,userPhoto,"default");
                            DatabaseReference mDatabaseRooms = dbReferenceAllCurrentUser.child(memberNewKey);
                            mDatabaseRooms.setValue(member);

                            Toast.makeText(context, "Successful access!", Toast.LENGTH_SHORT).show();

                            dialog.cancel();

                            context.startActivity(i);
                        } else {
                            Toast.makeText(context, "Incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                } else {
                    Intent i = new Intent(context, RoomActivity.class);
                    i.putExtra("idActualRoom", room.getId());
                    i.putExtra("room_name", room.getName());
                    context.startActivity(i);
                }
            }
        });

        return listViewItem;
    }
}
