package com.voteandeat.voteandeat.Room;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.R;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.RoomActivity;

import java.util.List;

public class RoomList  extends ArrayAdapter<Room> {
    private Activity context;
    private List<Room> roomList;
    DatabaseReference databaseReference;

    String userName;

    public RoomList(Activity context, List<Room> roomList, DatabaseReference databaseReference) {
        super(context, R.layout.activity_votes,roomList);
        this.context = context;
        this.roomList = roomList;
        this.databaseReference = databaseReference;
    }
    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.simple_room_item,null,true);

        final TextView tvRoomNameList = listViewItem.findViewById(R.id.tvRoomNameList);
        final TextView tvMemebersList = listViewItem.findViewById(R.id.tvMemebersList);

        final Room room = roomList.get(pos);
        tvRoomNameList.setText(room.getName());

        //Count Members
        tvMemebersList.setText("3 Members");

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RoomActivity.class);
                i.putExtra("idActualRoom", room.getId());
                i.putExtra("room_name", room.getName());
                context.startActivity(i);
            }
        });

        return listViewItem;
    }
}
