package barqsoft.footballscores.endpoints;

import java.io.IOException;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by Andrés on 9/15/15.
 */
public class FootballDataService {

    FootballDataApi mApi;

    private interface FootballDataApi {
        @GET("/alpha/teams/{team_id}")
        Call<GetTeamInformationResponse> getTeamInformation(
                @Header("X-Auth-Token") String apiKey,
                @Path("team_id") String teamId
        );
    }

    public FootballDataService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.football-data.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mApi = retrofit.create(FootballDataApi.class);
    }

    public GetTeamInformationResponse getTeamInformation(String apiKey, String teamId) throws IOException {
        return mApi.getTeamInformation(apiKey, teamId).execute().body();
    }

}
