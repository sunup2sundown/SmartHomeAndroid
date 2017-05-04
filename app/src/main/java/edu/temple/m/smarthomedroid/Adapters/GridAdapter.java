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
import edu.temple.m.smarthomedroid.Objects.Sensor;
import edu.temple.m.smarthomedroid.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Typeface.BOLD;

/**
 * Created by quido on 4/16/17.
 */

public class GridAdapter<E> extends ArrayAdapter<E> {
        private Context context;

        /**
         * Constructor Method
         *
         * @param context
         * @param elts
         */
        public GridAdapter(Context context, ArrayList<E> elts) {
                super(context, android.R.layout.simple_list_item_1,elts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                final E elt = getItem(position);

                if (convertView == null) {
                        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_relay_grid, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.houseN);
                name.setTextSize(20);
                TextView stt = (TextView) convertView.findViewById(R.id.textView5);
                stt.setTextSize(20);
                try {
                    if (elt instanceof Sensor) {
                        name.setText(((Sensor) elt).getName());
                        stt.setText(String.valueOf(((Sensor) elt).getValue()));
                    } else if (elt instanceof Relay2) {
                        name.setText(((Relay2) elt).getName());
                        if (((Relay2) elt).getStatus()) {
                            stt.setText("ON");
                        } else {
                            stt.setText("OFF");
                        }
                    }
                } catch (ClassCastException e){
                    e.printStackTrace();
                }

                return convertView;
        }
}
