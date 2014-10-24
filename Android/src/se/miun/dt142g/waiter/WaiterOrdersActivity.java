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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.EntityRep.TableHasDish;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.TableDishRelations;
import se.miun.dt142g.data.handler.Dishes;

/**
 *
 * @author Johannes
 */
public class WaiterOrdersActivity extends BaseActivity {
    private ListView listView = null;
    private ArrayAdapter<Dish> orders = null;
    private final List<Dish> values = new ArrayList<Dish>();
    
    static List<TableHasDish> tableOrder;
    static TableOrder order = null;
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
            for(TableHasDish hd : tableOrder) {
                Dish dish = dishes.getDish(hd.getDish().id);
                dish.setSpecial(hd.getDish().special);
                for(int i = 0; i < hd.getDish().dishCount; i++) {
                    values.add(dish);
                }
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
           
        makeChoiceOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public synchronized void onClick(DialogInterface dialog, int index) {
                Dish m = dishes.getDishByIndex(index);
                if(m.isInStock()){
                    Dish p = values.get(position);
                    values.set(position, m);
                    orders.notifyDataSetChanged();
                    tv.setTextColor(Color.BLACK);
                    orderItem.setBackgroundColor(Color.WHITE);
                    int dishIndex = 0;
                    for(TableHasDish hd : tableOrder) {
                        if(p.getId() == hd.getDish().id) {
                            if(m.getId() < 0 || hd.getDish().id < 0) 
                                continue;
                            //dishes.add(-p.getId()-1);
                            if(hd.getDish().dishCount > 1 && hd.getDish().id != m.getId()) {
                                hd.getDish().dishCount--;
                                newBtnClick.onClick(null, index);
                                return;
                            }
                            for(TableHasDish hd2 : WaiterTableActivity.tableOrdersLocal) {
                                if(hd2.getTableOrder().getTable() == hd.getTableOrder().getTable() && hd.getDish().id == hd2.getDish().id) {
                                    hd2.setDish(m);
                                    hd2.getTableOrder().setTimeOfOrder(new Date());
                                }
                            }
                            hd.setDish(m);//dishIndex, m.getId());
                            hd.getTableOrder().setTimeOfOrder(new Date());
                            DataService.updateServer();
                            break;
                        }
                        dishIndex++;
                    }
                }
                tv.setTextColor(Color.BLACK);
                orderItem.setBackgroundColor(Color.WHITE);
            }
        });
    }
    public void newOrder(View newBtn) {
        makeChoiceOfMenu(newBtnClick);
    }
    
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("status", "ordered");
        setResult(WaiterTableActivity.RESPONSE, data);
        finish();
    }

    private synchronized void makeChoiceOfMenu(DialogInterface.OnClickListener blob) {
            
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Väj en meny till vår gäst");
        builder.setItems(dishes.toCharSequence(), blob);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                
            }
        });
        builder.show();
    }
    
    public void toggleSpecial(View orderItem) {
        int position = listView.getPositionForView(orderItem);
        if(orderItem instanceof ImageView){
            ImageView special = (ImageView)orderItem;
            if (special.getTag().equals(R.drawable.special)) {
                special.setImageResource(R.drawable.special_gray);
                special.setTag(R.drawable.special_gray);
                
                int index = 0;
                for(TableHasDish hd : tableOrder) {
                    if(position-index > -1 && position-index < hd.getDish().dishCount) {
                        for(TableHasDish hd2 : WaiterTableActivity.tableOrdersLocal) {
                            if(hd2.getTableOrder().getTable() == hd.getTableOrder().getTable() && hd.getDish().id == hd2.getDish().id) {
                                hd2.getDish().special = false;
                            }
                        }
                        hd.getDish().special = false;
                        break;
                    }
                    else
                        index += hd.getDish().dishCount;
                }
                //tableOrder.get(position).getDish().special = false;
                DataService.updateServer();
            }
            else {
                special.setTag(R.drawable.special);
                special.setImageResource(R.drawable.special);
                
                int index = 0;
                for(TableHasDish hd : tableOrder) {
                    if(position-index > -1 && position-index < hd.getDish().dishCount) {
                        for(TableHasDish hd2 : WaiterTableActivity.tableOrdersLocal) {
                            if(hd2.getTableOrder().getTable() == hd.getTableOrder().getTable() && hd.getDish().id == hd2.getDish().id) {
                                hd2.getDish().special = true;
                            }
                        }
                        hd.getDish().special = true;
                        break;
                    }
                    else
                        index += hd.getDish().dishCount;
                }
                // tableOrder.get(index).getDish().special = true;
                DataService.updateServer();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tableOrder = null;
    }
    DialogInterface.OnClickListener newBtnClick =  new DialogInterface.OnClickListener() {
            @Override
            public synchronized void onClick(DialogInterface dialog, int which) {
                Dish m = dishes.getDishByIndex(which);
                if (m.isInStock()){
                    if(dialog != null) {
                        values.add(m);
                        orders.notifyDataSetChanged();
                    }
                    
                    TableHasDish hd = new TableHasDish();
                    hd.setDish(m);
                    order.setTimeOfOrder(new Date());
                    hd.setTableOrder(order);
                    tableOrder.add(hd);

                    
                    DataSource ds = DataService.getDataSource();
                    if(ds instanceof TableDishRelations) {
                        ((TableDishRelations)ds).getRelations().add(hd);
                    }
                    DataService.updateServer();
                    
                    int loc = 0;
                    for(TableHasDish hdtmp : WaiterTableActivity.tableOrdersLocal.getRelations()){
                        if(hdtmp.getDish().id == hd.getDish().id && hdtmp.getTableOrder().getTable() == hd.getTableOrder().getId()) {
                            WaiterTableActivity.tableOrdersLocal.getRelations().set(loc, hd);
                            return;
                        }
                        loc++;
                    }
                    WaiterTableActivity.tableOrdersLocal.getRelations().add(hd);
                }
            }
        };
}
