package com.voteandeat.voteandeat.GoogleAPI;

import com.voteandeat.voteandeat.GoogleAPI.Model.Results;
import com.voteandeat.voteandeat.GoogleAPI.Remote.IGoogleAPIService;
import com.voteandeat.voteandeat.GoogleAPI.Remote.RetrofitClient;

public class Common {

    public static Results currentResult;

    private static final String GOOGlE_API_URL = "https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGlE_API_URL).create(IGoogleAPIService.class);
    }
}
