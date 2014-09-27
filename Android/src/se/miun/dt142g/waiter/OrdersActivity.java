/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.waiter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import se.miun.dt142g.BaseActivity;

import se.miun.dt142g.R;
import se.miun.dt142g.data.Menu;
import se.miun.dt142g.data.Menus;

/**
 *
 * @author Johannes
 */
public class OrdersActivity extends BaseActivity {
    private ListView listView;
    private ArrayAdapter<Menu> orders = null;
    private final List<Menu> values = new ArrayList<Menu>();
    private final Menus availableMenus = new Menus();
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
        
        if(!availableMenus.readMenus())
            Toast.makeText(this, "Databasen var inte åtkommlig, du kan inte ta emot din beställning just nu", Toast.LENGTH_LONG).show();
        // Get ListView ob ject from xml
        listView = (ListView) findViewById(R.id.orderView);

        // Defined Array values to show in ListView
        values.add(availableMenus.getMenu("Fisk"));
        values.add(availableMenus.getMenu("Gröt"));
        values.add(availableMenus.getMenu("Spagetti"));
        values.add(availableMenus.getMenu("Potatisbullar"));
        
        orders = new ArrayAdapter<Menu>(this,
          R.layout.order_list_item, R.id.orderText1, values);

        // Assign adapter to ListView
        listView.setAdapter(orders);
    }
    
    public void removeBtnClicked(View btn) {
        int position = listView.getPositionForView(btn);
        
        Menu value = values.get(position);
        
        if(value.deleted())  {
            values.remove(position);
            orders.remove(value);
        }
        else
            value.delete();
        orders.notifyDataSetChanged();
    }
    public void orderDetails(final View orderItem) {
        final TextView tv = (TextView)orderItem.findViewById(R.id.orderText1);
        
        final int position = listView.getPositionForView(orderItem);
        // ListView Clicked item value
        Menu itemValue = orders.getItem(position);
        
        if(itemValue.deleted()) {
            tv.setTextColor(Color.BLACK);
            orderItem.setBackgroundColor(Color.WHITE);
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
        makeChoiseOfMenu(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Menu m = availableMenus.getMenu(which);
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
                Menu m = availableMenus.getMenu(which);
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
        if(!availableMenus.readMenus())
            Toast.makeText(this, "Databasen var inte åtkommlig, du kan inte ta emot din beställning just nu", Toast.LENGTH_LONG).show();
        //CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Väj en meny till vår gäst");
        builder.setItems(availableMenus.toCharSequence(), blob);
        builder.show();
    }
    
}
