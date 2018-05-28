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

import com.squareup.picasso.Picasso;
import com.voteandeat.voteandeat.GoogleAPI.Model.PlaceDetail;
import com.voteandeat.voteandeat.GoogleAPI.Remote.IGoogleAPIService;
import com.voteandeat.voteandeat.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

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

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        //PHOTO
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0){
            Picasso.get().load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000)) //because getPhotos() is array so we will take first item
            .placeholder(R.drawable.ic_panorama_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);

        }

        //RATING
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating())){
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }else{
            ratingBar.setVisibility(View.GONE);
        }

        //OPENING HOURS
        if(Common.currentResult.getOpening_hours() != null){
            opening_hours.setText("Open now: " +Common.currentResult.getOpening_hours().getOpen_now());
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
