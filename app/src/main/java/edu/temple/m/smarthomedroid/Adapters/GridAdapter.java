package edu.temple.m.smarthomedroid.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import edu.temple.m.smarthomedroid.Objects.Relay;
import edu.temple.m.smarthomedroid.Objects.Relay2;
import edu.temple.m.smarthomedroid.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Typeface.BOLD;

/**
 * Created by quido on 4/16/17.
 */

public class GridAdapter extends ArrayAdapter<Relay2> {
        private Context context;
        private boolean useList = true;

        /**
         * Constructor Method
         *
         * @param context
         * @param relays
         */
        public GridAdapter(Context context, ArrayList<Relay2> relays) {
                super(context, android.R.layout.simple_list_item_1,relays);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                final Relay2 relay2 = (Relay2) getItem(position);

                if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_relay_grid, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.houseN);
                name.setTextSize(25);
                name.setTypeface(name.getTypeface(),BOLD);
                TextView stt = (TextView) convertView.findViewById(R.id.textView5);
                stt.setTextSize(25);
                name.setText(relay2.getName());
                if (relay2.getStatus()) {
                        stt.setText("ON");
                } else {
                        stt.setText("OFF");
                }

                return convertView;
        }
}
