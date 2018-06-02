package com.voteandeat.voteandeat.GoogleAPI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.GoogleAPI.Model.PlaceDetail;
import com.voteandeat.voteandeat.GoogleAPI.Remote.IGoogleAPIService;
import com.voteandeat.voteandeat.R;
import com.voteandeat.voteandeat.Room.Model.Member;
import com.voteandeat.voteandeat.Room.Model.Room;
import com.voteandeat.voteandeat.Room.Model.VotePlace;
import com.voteandeat.voteandeat.RoomActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlace extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    TextView opening_hours, place_address, place_name;
    Button btnViewOnMap;

    IGoogleAPIService mService;

    PlaceDetail myPlace;

    String idRoom;

    //VOTE
    String addressVotePlace="";
    String nameVotePlace="";
    String photoUrlVotePlace="https://firebasestorage.googleapis.com/v0/b/voteandeat.appspot.com/o/icons8-image-file-512.png?alt=media&token=f8e0208d-f94c-4849-bdd1-314bfa115b73";
    Double latitudeVotePlace=0.0;
    Double longitudeVotePlace=0.0;
    String starsVotePlace="";
    Boolean openNowVotePlace=false;
    String mapUrlVotePlace="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        //get idRoomIfExists
        idRoom = getIntent().getExtras().get("idActualRoom").toString();


        mService = Common.getGoogleAPIService();

        photo = findViewById(R.id.photo);
        ratingBar = findViewById(R.id.ratingBar);
        place_address = findViewById(R.id.place_address);
        place_name = findViewById(R.id.place_name);
        opening_hours = findViewById(R.id.place_open_hour);
        btnViewOnMap = findViewById(R.id.btn_show_map);

        //Empty all views
        place_name.setText("");
        place_address.setText("");
        opening_hours.setText("");

        if(idRoom != null){
            btnViewOnMap.setText("ADD PLACE");

            btnViewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ViewPlace.this, RoomActivity.class);

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Rooms").child(idRoom).child("Votes").child("VotePlace");
                    String  room_key = mDatabase.push().getKey();

                    String idCurrentUser = mAuth.getCurrentUser().getUid();
                    mapUrlVotePlace = myPlace.getResult().getUrl();


                    VotePlace votePlace = new VotePlace(idCurrentUser,addressVotePlace,nameVotePlace,photoUrlVotePlace,latitudeVotePlace,longitudeVotePlace,starsVotePlace,mapUrlVotePlace,openNowVotePlace);
                    DatabaseReference mDatabaseVotePlace = mDatabase.child(room_key);
                    mDatabaseVotePlace.setValue(votePlace);
                    Toast.makeText(getApplicationContext(), "Vote created succesfully", Toast.LENGTH_SHORT).show();

                    intent.putExtra("idActualRoom",  idRoom);
                    startActivity(intent);
                }
            });
        }else{
            btnViewOnMap.setText("VIEW ON MAP");
            btnViewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myPlace.getResult().getUrl()));
                    startActivity(mapIntent);
                }
            });
        }



        //PHOTO
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0){
            Picasso.get().load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000)) //because getPhotos() is array so we will take first item
            .placeholder(R.drawable.ic_panorama_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);

            photoUrlVotePlace = getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000);

        }

        //RATING
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating())){
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));

            starsVotePlace = Common.currentResult.getRating();
        }else{
            ratingBar.setVisibility(View.GONE);
        }

        //OPENING HOURS
        if(Common.currentResult.getOpening_hours() != null){
            opening_hours.setText("Open now: " +Common.currentResult.getOpening_hours().getOpen_now());

            openNowVotePlace =  Boolean.valueOf(Common.currentResult.getOpening_hours().getOpen_now());
        }else{
            opening_hours.setVisibility(View.GONE);
        }

        //User service to fetch Address and Name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        myPlace = response.body();

                        place_address.setText(myPlace.getResult().getFormatted_address());
                        place_name.setText(myPlace.getResult().getName());

                        addressVotePlace = myPlace.getResult().getFormatted_address();
                        nameVotePlace = myPlace.getResult().getName();
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
