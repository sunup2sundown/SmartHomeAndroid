package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by quido on 4/23/17.
 */

public class DataHolder {
    private static DataHolder dataObject = null;

    public DataHolder() {
        // left blank intentionally
    }

    public static DataHolder getInstance() {
        if (dataObject == null)
            dataObject = new DataHolder();
        return dataObject;
    }
    private String distributor_id;
    private int toggle;

    public String getDistributor_id() {
        toggle =0;
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
        toggle=1;
    }
    public int gettoggle(){return this.toggle;};
}
