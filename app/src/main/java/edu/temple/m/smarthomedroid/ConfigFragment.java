package edu.temple.m.smarthomedroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by M on 3/16/2017.
 */

public class ConfigFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.thirdlayout,container,false);
        return v;
    }

    //TODO: Expandable List view of Houses->Boards->Peripheral types->Peripheral Names
}
