package com.example.dani.slidsaw_beta;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by dani on 14/01/16.
 */

public class ItemAdapter extends ArrayAdapter<Item>
{
    public ItemAdapter(Context context, int layout_resource, ArrayList<Item> data) {
        super(context, layout_resource, R.id.item_name, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        Item item = getItem(position);

        // Obtenim referències a les parts de l'item de la llista
        // a la posició 'position'
        TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
        TextView item_time   = (TextView) convertView.findViewById(R.id.item_time);

        // Transferim dades del item al view que sortirà a la llista
        item_name.setText(item.getName());
        item_time.setText(item.getTime());

        return convertView;
    }
}
