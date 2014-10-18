/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.waiter;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.InvalidCredentialsException;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.TableOrders;
/**
 * 
 * @author Johannes
 */
public class WaiterActivity extends BaseActivity {
    public static final int RESPONSE = 0;
    
    private final TableOrders tableOrders = new TableOrders();
    private final List<Button> tableButtons = new ArrayList<Button>();
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            
        }
    };
    

    public WaiterActivity() {
        
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_menu);
        
        DataService.setSyncSpeed(DataSourceListener.DEFAULT_SYNC_SPPED);
        DataService.setAutoLoad(false);
        
        DataService.setDataSource(tableOrders);
        DataService.setHandler(handler);
        
        updateTableOrderObject(DataService.getDataSource());
        
        WaiterOrdersActivity.dishes = tableOrders.getDishes();
        
        updateBtnColors(false);
    }
    public void table_click(View v) throws InvalidCredentialsException {
        if(v.getClass() != Button.class)
            throw new InvalidCredentialsException("No button click. The view passed is invalid");
        Button btn = (Button)v;
        
        int tableNr = Integer.parseInt(btn.getText().toString().replace("Bord ", ""));
        WaiterOrdersActivity.tableOrder = tableOrders.getTable(tableNr-1);
        Intent ordersActivity = new Intent(getApplicationContext(), WaiterOrdersActivity.class);
        ordersActivity.putExtra("bord_str", btn.getText());
        startActivityForResult(ordersActivity, RESPONSE);
    }
    
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(resCode, resCode, data);
        if(resCode == RESPONSE) {
            // To get data use
            // data.getExtras().get("data_name");
            Toast.makeText(getApplicationContext(),
                      "Result passed", Toast.LENGTH_LONG)
                      .show();
            
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataService.setAutoLoad(true);
    }

    private void updateBtnColors(boolean b) {
        DataSource dataSource = DataService.getDataSource();
        if(b)
            updateTableOrderObject(dataSource);
        LinearLayout btnsParent = (LinearLayout)findViewById(R.id.order_btns);
        for(int i = btnsParent.getChildCount(); i > 0; i--) {
            Button btn = (Button)btnsParent.getChildAt(i-1);
            int tableNr = Integer.parseInt(btn.getText().toString().replace("Bord ", ""));
            if(tableOrders.getTable(tableNr-1).getOrderedDishes().size() > 0)
                btn.setBackgroundResource(R.drawable.green_button);
            else
                btn.setBackgroundResource(R.drawable.default_button);
            tableButtons.add(btn);
        }
    }

    private void updateTableOrderObject(DataSource dataSource) {
        Thread threadSync = new Thread(DataService.updateOnce);
        
        DataSource tmpSrc = DataService.getDataSource();
        try {
            DataService.setDataSource(dataSource);
            threadSync.start();
            threadSync.join(5000);
            if(threadSync.isAlive())
                threadSync.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(WaiterActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IllegalThreadStateException ex) {}
        finally {
            DataService.setDataSource(tmpSrc);
        }
    }
}
