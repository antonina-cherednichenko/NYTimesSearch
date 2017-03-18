package codepath.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;


@Parcel
public class Multimedia {
    @SerializedName("subtype")
    String subtype;

    @SerializedName("url")
    String url;

    public String getSubtype() {
        return subtype;
    }

    public String getUrl() {
        return "http://www.nytimes.com/" + url;
    }
}
