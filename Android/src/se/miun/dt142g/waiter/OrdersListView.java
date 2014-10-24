/*
 * Title:
 * Subject: 
 */

package se.miun.dt142g.waiter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.R;

/**
 *
 * @author Simple
 */
public class OrdersListView extends ArrayAdapter<Dish>{
    
    public OrdersListView(Context context, int resource, int textViewResourceId, int textViewResourceId2, List<Dish> objects) {
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
        Dish dish = getItem(position);
        priceView.setText(Float.toString(dish.getPrice()) + " :-");
        ImageView img = ((ImageView)convertView.findViewById(R.id.special_img));
        if(dish.getSpecial()) {
            img.setImageResource(R.drawable.special);
            img.setTag(R.drawable.special);
        }
        else {
            img.setImageResource(R.drawable.special_gray);
            img.setTag(R.drawable.special_gray);
        }
        return convertView;
    }
}
