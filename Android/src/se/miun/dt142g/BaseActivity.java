/*
 * This BaseActivity implements all common components and methods 
 * for all activities
 */
package se.miun.dt142g;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 *
 * @author Johannes
 */
public class BaseActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.waiter && !(this instanceof se.miun.dt142g.waiter.WaiterActivity)) {
            System.out.println("Waiter loading view...");
            Intent ordersActivity = new Intent(getApplicationContext(), se.miun.dt142g.waiter.WaiterActivity.class);
            startActivity(ordersActivity);
            return true;
        }
        if (id == R.id.kitchen && !(this instanceof se.miun.dt142g.kitchen.Orders)) {
            System.out.println("Kitcgen loading view...");
            Intent ordersActivity = new Intent(getApplicationContext(), se.miun.dt142g.kitchen.Orders.class);
            startActivity(ordersActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
