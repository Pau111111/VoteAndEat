package com.voteandeat.voteandeat.GoogleAPI.Remote;

import com.voteandeat.voteandeat.GoogleAPI.Model.MyPlaces;
import com.voteandeat.voteandeat.GoogleAPI.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);
}
