package codepath.com.nytimessearch.models;


import org.parceler.Parcel;

import java.util.Calendar;

@Parcel
public class FilterData {

    String query;
    Calendar cal;
    String order;
    boolean isSports;
    boolean isArts;
    boolean isFashion;


    public FilterData() {
        this.query = "";
        this.order = "";
        this.cal = Calendar.getInstance();
        this.isArts = false;
        this.isSports = false;
        this.isFashion = false;
    }

    public FilterData(Calendar cal, String order) {
        this.cal = cal;
        this.order = order;
    }

    public void setArts(boolean arts) {
        isArts = arts;
    }

    public void setFashion(boolean fashion) {
        isFashion = fashion;
    }

    public void setSports(boolean sports) {
        isSports = sports;
    }

    public void clearQuery() {
        this.query = "";
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Calendar getCal() {
        return cal;
    }

    public boolean isArts() {
        return isArts;
    }

    public boolean isFashion() {
        return isFashion;
    }

    public boolean isSports() {
        return isSports;
    }

    public String getOrder() {
        return order;
    }

    public String getQuery() {
        return query;
    }
}
