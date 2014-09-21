/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.waiter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import se.miun.dt142g.R;
import se.miun.dt142g.data.Order;

/**
 *
 * @author Johannes
 */
public class OrdersActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<Order> orders = null;
    private final List<Order> values = new ArrayList<Order>();
    /**
     * Called when the activity is first created.
     * @param icicle The bundle for current activity
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
       
        setContentView(R.layout.order_menu);
        
        Intent thisActivity = getIntent();
        setTitle(thisActivity.getExtras().getString("bord_str"));
        
        // Get ListView ob ject from xml
        listView = (ListView) findViewById(R.id.orderView);

        // Defined Array values to show in ListView
        values.add(new Order("Fisk (2)"));
        values.add(new Order("Gröt (1)"));
        values.add(new Order("Spagetti (1)"));
        values.add(new Order("Potatisbullar (1)"));
        
        orders = new ArrayAdapter<Order>(this,
          R.layout.order_list_item, R.id.orderText1, values);

        // Assign adapter to ListView
        listView.setAdapter(orders);
    }
    
    public void removeBtnClicked(View btn) {
        int position = listView.getPositionForView(btn);
        
        Order value = values.get(position);
        
        if(value.deleted())  {
            values.remove(position);
            orders.remove(value);
        }
        else
            value.delete();
        orders.notifyDataSetChanged();
    }
    public void orderDetails(View orderItem) {
        TextView tv = (TextView)orderItem.findViewById(R.id.orderText1);
        
        int position = listView.getPositionForView(orderItem);
        // ListView Clicked item value
        Order itemValue = orders.getItem(position);
        
        if(itemValue.deleted()) {
            itemValue.restore();
            orders.notifyDataSetChanged();
            return;
        }
        
        tv.setTextColor(Color.BLACK);
        orderItem.setBackgroundColor(Color.DKGRAY);
        
        // Show Alert 
        Toast.makeText(getApplicationContext(),
           "Index :"+position+"  Val: " +itemValue , Toast.LENGTH_LONG)
           .show();       
    }
    
    @Override
    public void onBackPressed(){
        Intent data = new Intent();
        data.putExtra("status", "ordered");
        setResult(1337, data);
        finish();
    }
}
