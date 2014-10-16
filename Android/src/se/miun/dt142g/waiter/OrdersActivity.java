/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.waiter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.datahandler.DataSource;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.EntityHandler.Dishes;

/**
 *
 * @author Johannes
 */
public class OrdersActivity extends BaseActivity {
    private ListView listView;
    private ArrayAdapter<Dish> orders = null;
    private final List<Dish> values = new ArrayList<Dish>();
    private final Dishes availableMenus = new Dishes();
    
    /**
     * Called when the activity is first created.
     * @param icicle The bundle for current activity
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
       
        setContentView(R.layout.order_menu);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        
        Intent thisActivity = getIntent();
        setTitle(thisActivity.getExtras().getString("bord_str"));
        

        // Get ListView ob ject from xml
        listView = (ListView) findViewById(R.id.orderView);

        // Defined Array values to show in ListView
        
        
        
        orders = new OrdersListView(this,
          R.layout.order_list_item, R.id.orderText1, R.id.foodPrice, values);
        
        // Assign adapter to ListView
        listView.setAdapter(orders);
    }
    
    public void removeBtnClicked(View btn) {
        int position = listView.getPositionForView(btn);
        
        Dish value = values.get(position);
        
        if(value.isDeleted())  {
            values.remove(position);
            orders.remove(value);
        }
        else
            value.setDeleted(true);
        orders.notifyDataSetChanged();
    }
    public void orderDetails(final View orderItem) {
        final TextView tv = (TextView)orderItem.findViewById(R.id.orderText1);
        
        final int position = listView.getPositionForView(orderItem);
        // ListView Clicked item value
        Dish itemValue = orders.getItem(position);
        
        if(itemValue.isDeleted()) {
            tv.setTextColor(Color.BLACK);
            orderItem.setBackgroundColor(Color.WHITE);
            itemValue.setDeleted(false);
            orders.notifyDataSetChanged();
            return;
        }
        
        tv.setTextColor(Color.BLACK);
        orderItem.setBackgroundColor(Color.DKGRAY);
        
        // Show Alert 
        Toast.makeText(getApplicationContext(),
           "Index :"+position+"  Val: " +itemValue , Toast.LENGTH_LONG)
           .show();       
        makeChoiseOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dish m = availableMenus.getDishByIndex(which);
                values.set(position, m);
                orders.notifyDataSetChanged();
                tv.setTextColor(Color.BLACK);
                orderItem.setBackgroundColor(Color.WHITE);
            }
        });
    }
    public void newOrder(View newBtn) {
        makeChoiseOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dish m = availableMenus.getDishByIndex(which);
                values.add(m);
                orders.notifyDataSetChanged();
            }
        });
    }
    
    @Override
    public void onBackPressed(){
        Intent data = new Intent();
        data.putExtra("status", "ordered");
        setResult(1337, data);
        finish();
    }

    private void makeChoiseOfMenu(DialogInterface.OnClickListener blob) {
        availableMenus.load();
            
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Väj en meny till vår gäst");
        builder.setItems(availableMenus.toCharSequence(), blob);
        builder.show();
    }
    
    public void toggleSpecial(View v){
        if(v instanceof ImageView){
            ImageView special = (ImageView)v;
            if (special.getTag().equals(R.drawable.special)) {
                special.setImageResource(R.drawable.special_gray);
                special.setTag(R.drawable.special_gray);
            }
            else {
                special.setTag(R.drawable.special);
                special.setImageResource(R.drawable.special);
            }
        }
    }
}
