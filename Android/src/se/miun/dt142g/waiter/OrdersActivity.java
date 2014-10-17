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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.Dishes;
import se.miun.dt142g.data.handler.TableOrders;

/**
 *
 * @author Johannes
 */
public class OrdersActivity extends BaseActivity {
    private ListView listView = null;
    private ArrayAdapter<Dish> orders = null;
    private final List<Dish> values = new ArrayList<Dish>();
    
    private final TableOrders tableOrders;
    private final Dishes dishes;
    private int tableNr;

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                if(data.containsKey("connectionError")) {
                    // TODO: Print Toast message here
                }
                if(data.containsKey("dataUpdated") && data.getInt("dataUpdated") == DataSourceListener.UPDATE_CALL) {
                    if(listView != null) {
                        // TODO Do stuff..
                    }
                }
            }

        }
        

    };
    DataSourceListener background = null;

    public OrdersActivity() {
        
        tableOrders = new TableOrders();
        dishes = tableOrders.getDishes();
        
    }
    
    /**
     * Called when the activity is first created.
     * @param icicle The bundle for current activity
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        dishesDataSource = new DataSourceListener(availableMenus);
        dishesDataSource.setHandler(handler);
        dishesDataSource.setIntervallSpeed(DataSourceListener.SLOW_SYNC_SPPED);
        dishesDataSource.start();
        setContentView(R.layout.order_menu);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        
        Intent thisActivity = getIntent();
        setTitle(thisActivity.getExtras().getString("bord_str"));
        tableNr = Integer.parseInt(thisActivity.getExtras().getString("bord_str").replace("Bord ", ""));

        // Get ListView ob ject from xml
        listView = (ListView) findViewById(R.id.orderView);

        // Defined Array values to show in ListView
        
        
        
        orders = new OrdersListView(this,
          R.layout.order_list_item, R.id.orderText1, R.id.foodPrice, values);
        
        // Assign adapter to ListView
        listView.setAdapter(orders);
        
        background = new DataSourceListener(tableOrders, DataSourceListener.SLOW_SYNC_SPPED);
        background.setHandler(handler);
        background.start();
    }
    
    public void removeBtnClicked(View btn) {
        int position = listView.getPositionForView(btn);
        
        Dish value = values.get(position);
        
        if(value.isDeleted())  {
            values.remove(position);
            orders.remove(value);
            // TODO Update Tableorder here
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
                Dish m = dishes.getDishByIndex(which);
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
                Dish m = dishes.getDishByIndex(which);
                values.add(m);
                orders.notifyDataSetChanged();
                tableOrders.getTableOrderByIndex(tableNr-1).getOrderedDishes().add(m.getId());
                background.writeData();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("status", "ordered");
        setResult(1337, data);
        finish();
    }

    private void makeChoiseOfMenu(DialogInterface.OnClickListener blob) {
            
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Väj en meny till vår gäst");
        builder.setItems(dishes.toCharSequence(), blob);
        builder.show();
    }
    
    public void toggleSpecial(View v) {
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
