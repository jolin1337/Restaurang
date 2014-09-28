/*
 * Title:
 * Subject: 
 */

package se.miun.dt142g.waiter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import se.miun.dt142g.data.Menu;

/**
 *
 * @author Simple
 */
public class OrdersListView extends ArrayAdapter<Menu>{
    
    public OrdersListView(Context context, int resource, int textViewResourceId, int textViewResourceId2, List<Menu> objects) {
        super(context, resource, textViewResourceId, objects);
        layoutViewResourceId = resource;
        this.textViewResourceId = textViewResourceId;
        this.textViewResourceId2 = textViewResourceId2;
    }
    
    private final int layoutViewResourceId;
    private final int textViewResourceId;
    private final int textViewResourceId2;
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = super.getView(position, convertView, parent);
        if (convertView == null)
            return null;
        TextView priceView = (TextView)(convertView.findViewById(textViewResourceId2));
        priceView.setText(Float.toString(getItem(position).price) + " :-");
        return convertView;
    }
}
