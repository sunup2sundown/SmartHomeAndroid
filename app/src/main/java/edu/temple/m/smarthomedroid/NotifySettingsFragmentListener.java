package edu.temple.m.smarthomedroid;

/**
 * Created by Jhang Myong Ja on 4/30/2017.
 */

public interface NotifySettingsFragmentListener {
    void notifyHouseRemoved(int index); // notify settings fragment of data changes
    void notifyHouseAdded(String houseName);
    void notifyHouseNameChanged(int i, String newHouseName);
}
