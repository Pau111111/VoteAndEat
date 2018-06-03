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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.R;
import com.voteandeat.voteandeat.Room.Model.Member;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.RoomActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberList  extends ArrayAdapter<Member> {
    private Activity context;
    private List<Member> memberList;
    DatabaseReference databaseReference;

    String userName;

    public MemberList(Activity context, List<Member> memberList, DatabaseReference databaseReference) {
        super(context, R.layout.activity_room,memberList);
        this.context = context;
        this.memberList = memberList;
        this.databaseReference = databaseReference;
    }
    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.simple_member_item,null,true);

        final TextView tvMemberNameListRoom = listViewItem.findViewById(R.id.tvMemberNameListRoom);
        final ImageView memberPhotoListRoom = listViewItem.findViewById(R.id.memberPhotoListRoom);


        final Member member = memberList.get(pos);
        tvMemberNameListRoom.setText(member.getName());

        Picasso.get().load(member.getPhotoUrlUser())
                .placeholder(R.drawable.ic_image_room_vote_24dp)
                .error(R.drawable.ic_image_room_vote_24dp)
                .into(memberPhotoListRoom);

        return listViewItem;
    }
}
