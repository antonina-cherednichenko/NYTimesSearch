package codepath.com.nytimessearch.models;


import java.io.Serializable;
import java.util.Calendar;

public class FilterData implements Serializable {

    private String query;
    private Calendar cal;
    private String order;
    private boolean isSports;
    private boolean isArts;
    private boolean isFashion;


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
