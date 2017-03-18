package codepath.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

public class NYTimesResponse {

    @SerializedName("response")
    ArticleResponse response;

    public ArticleResponse getResponse() {
        return response;
    }
}
