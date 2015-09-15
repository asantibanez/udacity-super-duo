package barqsoft.footballscores.endpoints;

import com.google.gson.Gson;

/**
 * Created by Andrés on 9/15/15.
 */
public class GetTeamInformationResponse {

    String name;
    String code;
    String shortName;
    String squadMarketValue;
    String crestUrl;

    public String toJson() {
        return new Gson().toJson(this);
    }

}
