package barqsoft.footballscores.endpoints;

import android.util.Log;

import java.io.IOException;

import barqsoft.footballscores.domain.Team;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Andrés on 9/15/15.
 */
public class FootballDataService {

    FootballDataApi mApi;

    private interface FootballDataApi {
        @GET("/alpha/teams/{team_id}")
        Call<GetTeamInformationResponse> getTeamInformation(@Path("team_id") String teamId);
    }

    public FootballDataService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mApi = retrofit.create(FootballDataApi.class);
    }

    public Team getTeamInformation(String teamId) {


        mApi.getTeamInformation(teamId).enqueue(new Callback<GetTeamInformationResponse>() {
            @Override
            public void onResponse(Response<GetTeamInformationResponse> response) {
                Log.d("Response", response.body().toJson());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Response", t.getMessage());
            }
        });
        return null;
    }


}
