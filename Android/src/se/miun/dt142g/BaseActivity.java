/*
 * This BaseActivity implements all common components and methods 
 * for all activities
 */
package se.miun.dt142g;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import se.miun.dt142g.reservations.ReservationsActivity;
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
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().build());
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
        if (!onOptionsItemSelectedStatic(item, this))
            return super.onOptionsItemSelected(item);
        return true; 
    }
    
    public static boolean onOptionsItemSelectedStatic(MenuItem item, Activity act) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.waiter && !(act instanceof se.miun.dt142g.waiter.WaiterActivity)) {
            System.out.println("Waiter loading view...");
            Intent ordersActivity = new Intent(act.getApplicationContext(), se.miun.dt142g.waiter.WaiterActivity.class);
            act.startActivity(ordersActivity);
            return true;
        }
        if (id == R.id.kitchen && !(act instanceof se.miun.dt142g.kitchen.KitchenOrdersActivity)) {
            System.out.println("Kitchen loading view...");
            Intent ordersActivity = new Intent(act.getApplicationContext(), se.miun.dt142g.kitchen.KitchenOrdersActivity.class);
            act.startActivity(ordersActivity);
            return true;
        }
        if (id == R.id.reservations && 
                !(act.getClass() == ReservationsActivity.class &&
                ( (ReservationsActivity) act ) instanceof ReservationsActivity)) {
            System.out.println("Reservations loading view...");
            Intent reservationsActivity = new Intent(act.getApplicationContext(), se.miun.dt142g.reservations.ReservationsActivity.class);
            act.startActivity(reservationsActivity);
            return true;
        }  
        if (id == R.id.loginActivity && !(act instanceof se.miun.dt142g.LoginActivity)) {
            System.out.println("Login loading view...");
            Intent loginActivity = new Intent(act.getApplicationContext(), se.miun.dt142g.LoginActivity.class);
            act.startActivity(loginActivity);
            return true;
        }
        return false; 
    }
}
