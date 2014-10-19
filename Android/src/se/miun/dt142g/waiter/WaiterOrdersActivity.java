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
import java.util.Date;
import java.util.List;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.Dishes;
import se.miun.dt142g.data.handler.TableOrders;

/**
 *
 * @author Johannes
 */
public class WaiterOrdersActivity extends BaseActivity {
    private ListView listView = null;
    private ArrayAdapter<Dish> orders = null;
    private final List<Dish> values = new ArrayList<Dish>();
    
    static TableOrder tableOrder;
    static Dishes dishes;

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                if(data.containsKey("connectionError")) {
                    DataService.handleError(data.getInt("connectionError"));
                }
                if(data.containsKey("dataUpdated") && data.getInt("dataUpdated") == DataSourceListener.UPDATE_CALL) {
                }
            }

        }
        

    };

    public WaiterOrdersActivity() {
        
        //tableOrders = new TableOrders();
        //dishes = tableOrders.getDishes();
        
    }
    
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
//        setTitle(thisActivity.getExtras().getString("bord_str"));
        setTitle(thisActivity.getExtras().getString("bord_str"));

        // Get ListView ob ject from xml
        listView = (ListView) findViewById(R.id.orderView);

        
        
        orders = new OrdersListView(this,
          R.layout.order_list_item, R.id.orderText1, R.id.foodPrice, values);
        
        // Assign adapter to ListView
        listView.setAdapter(orders);
        
        
        DataService.setSyncSpeed(DataSourceListener.DEFAULT_SYNC_SPPED);
        //DataService.setDataSource(tableOrders);
        //DataService.setHandler(handler);

        if(listView != null) {
            orders.clear();
            values.clear();
            for(Integer dish : tableOrder.getOrderedDishes()) {
                values.add(dishes.getDish(dish));
            }
            orders.notifyDataSetChanged();
        }
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
//        Toast.makeText(getApplicationContext(),
//           "Index :"+position+"  Val: " +itemValue , Toast.LENGTH_LONG)
//           .show();       
        makeChoiseOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public synchronized void onClick(DialogInterface dialog, int which) {
                Dish m = dishes.getDishByIndex(which);
                Dish p = values.get(position);
                values.set(position, m);
                orders.notifyDataSetChanged();
                tv.setTextColor(Color.BLACK);
                orderItem.setBackgroundColor(Color.WHITE);
                List<Integer> dishes = tableOrder.getOrderedDishes();
                for(int dishIndex = dishes.size(); dishIndex > 0; dishIndex--) {
                    if(p.getId() == dishes.get(dishIndex-1)) {
                        if(m.getId() < 0 || dishes.get(dishIndex-1) < 0) continue;
                        dishes.add(-p.getId()-1);
                        dishes.set(dishIndex-1, m.getId());
                        tableOrder.setTimeOfOrder(new Date());
                        DataService.updateServer();
                        break;
                    }
                }
            }
        });
    }
    public void newOrder(View newBtn) {
        makeChoiseOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public synchronized void onClick(DialogInterface dialog, int which) {
                Dish m = dishes.getDishByIndex(which);
                values.add(m);
                orders.notifyDataSetChanged();
                
                tableOrder.getOrderedDishes().add(m.getId());
                tableOrder.setTimeOfOrder(new Date());
                DataService.updateServer();
            }
        });
    }
    
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("status", "ordered");
        setResult(WaiterTableActivity.RESPONSE, data);
        finish();
    }

    private synchronized void makeChoiseOfMenu(DialogInterface.OnClickListener blob) {
            
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
                tableOrder.setSpecial(true);
            }
            else {
                special.setTag(R.drawable.special);
                special.setImageResource(R.drawable.special);
                tableOrder.setSpecial(false);
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tableOrder = null;
    }
}
