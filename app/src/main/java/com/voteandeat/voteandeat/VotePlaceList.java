package com.voteandeat.voteandeat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.GoogleAPI.Common;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.UserVote;
import com.voteandeat.voteandeat.Room.Model.VotePlace;

import java.util.ArrayList;
import java.util.List;

class VotePlaceList  extends ArrayAdapter<VotePlace> {
    private Activity context;
    private List<VotePlace> votePlacesList;
    DatabaseReference databaseReference;
    DatabaseReference dbReferenceCheckboxUserVote;
    DatabaseReference mDatabaseUserVote;
    FirebaseAuth mAuth;
    String idCurrentUser;

    public VotePlaceList(Activity context, List<VotePlace> votePlacesList, DatabaseReference databaseReference) {
        super(context, R.layout.activity_room,votePlacesList);
        this.context = context;
        this.votePlacesList = votePlacesList;
        this.databaseReference = databaseReference;
    }
    public View getView(int pos, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.simple_place_item,null,true);

        mAuth = FirebaseAuth.getInstance();
        idCurrentUser = mAuth.getCurrentUser().getUid();

        final TextView voteNameLastStep = listViewItem.findViewById(R.id.voteNameLastStep);
        final ImageView voteImgLastStep = listViewItem.findViewById(R.id.voteImgLastStep);
        final CheckBox cbSelectPlaceUserVote= listViewItem.findViewById(R.id.cbSelectPlaceUserVote);

        final VotePlace votePlace = votePlacesList.get(pos);
        voteNameLastStep.setText(votePlace.getName());

        if(votePlace.getPhotoUrl() != "") {
            Picasso.get()
                    .load(votePlace.getPhotoUrl())
                    .placeholder(R.drawable.ic_image_room_vote_24dp)
                    .error(R.drawable.ic_image_room_vote_24dp)
                    .into(voteImgLastStep, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            //Picasso.get().load(votePlace.getPhotoUrl()).into(voteImgLastStep);
        }

        dbReferenceCheckboxUserVote = databaseReference.getParent();
        //-------------------CHECKBOX--------------------
        cbSelectPlaceUserVote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.d("Puchu", "Unchecked");
                    String userVoteKey = dbReferenceCheckboxUserVote.push().getKey();
                    UserVote userVote = new UserVote(idCurrentUser,votePlace.getIdVotePlace());
                    //DatabaseReference mDatabaseUserVote = databaseReference.child(votePlace.getIdVotePlace()).child("UserVote").child(userVoteKey);
                    mDatabaseUserVote = dbReferenceCheckboxUserVote.child("UserVote").child(userVoteKey);
                    mDatabaseUserVote.setValue(userVote);
                } else if(!isChecked){
                    mDatabaseUserVote.removeValue();
                }
            }
        });
        //-------------------CHECKBOX--------------------

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialog_show_place_vote,null);

                ImageView photo;
                RatingBar ratingBar;
                TextView openingHours, place_address, place_name;
                Button btnViewOnMap;

                photo = mView.findViewById(R.id.ivPlacePhotoVote);
                ratingBar = mView.findViewById(R.id.ratingBarVote);
                place_address = mView.findViewById(R.id.tvPlaceAddressVote);
                place_name = mView.findViewById(R.id.tvPlaceNameVote);
                openingHours = mView.findViewById(R.id.tvOpenHourVote);
                btnViewOnMap = mView.findViewById(R.id.btnShowOnMapVote);

                Picasso.get().load(votePlace.getPhotoUrl()).into(photo);

                btnViewOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(votePlace.getMapUrl()));
                        context.startActivity(mapIntent);
                    }
                });

                place_name.setText(votePlace.getName());
                place_address.setText(votePlace.getAddress());
                if(votePlace.openNow){
                    openingHours.setText("Open now");
                }else{
                    openingHours.setText("Closed now");
                }
                ratingBar.setRating(Float.parseFloat(votePlace.getStars()));

                mbuilder.setView(mView);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });

        return listViewItem;
    }
}
