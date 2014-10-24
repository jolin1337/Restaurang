/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.waiter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.InvalidCredentialsException;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.TableHasDish;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.TableDishRelations;
/**
 * 
 * @author Johannes
 */
public class WaiterTableActivity extends BaseActivity {
    public static final int SYNCDURATION_DELAY = 10000;
    public static final int RESPONSE = 0;
    
    private static final TableDishRelations tableOrders = new TableDishRelations();
    static final TableDishRelations tableOrdersLocal = new TableDishRelations();
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                if(data.containsKey("connectionError")) {
                    DataService.handleError(data.getInt("connectionError"));
                }
                if(data.containsKey("dataUpdated") && data.getInt("dataUpdated") == DataSourceListener.UPDATE_CALL) {
                    updateBtnColors(true);
                }
            }
        }
    };
    

    public WaiterTableActivity() {
        
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_menu);
        
        DataService.setSyncSpeed(DataSourceListener.DEFAULT_SYNC_SPPED);
        
        DataService.setDataSource(tableOrders);
        DataService.setHandler(handler);
        if(tableOrders.getTableSize() != 6)
            updateTableOrderObject(DataService.getDataSource());
        
        WaiterOrdersActivity.dishes = tableOrders.getDishes();
        
        updateBtnColors(true);
    }
    public void table_click(View v) throws InvalidCredentialsException {
        if(v.getClass() != Button.class)
            throw new InvalidCredentialsException("No button click. The view passed is invalid");
        Button btn = (Button)v;
        
        int tableNr = Integer.parseInt(btn.getText().toString().replace("Bord ", ""));
        WaiterOrdersActivity.tableOrder = tableOrders.getDishesFromTable(tableNr-1);
        WaiterOrdersActivity.order = tableOrders.getTableOrder(tableNr-1);
        Intent ordersActivity = new Intent(getApplicationContext(), WaiterOrdersActivity.class);
        ordersActivity.putExtra("bord_str", btn.getText());
        startActivityForResult(ordersActivity, RESPONSE);
    }
    
    @Override
    protected synchronized void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(resCode, resCode, data);
        if(resCode == RESPONSE) {
            
            updateBtnColors(true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataService.setAutoLoad(true);
    }

    private void updateBtnColors(boolean b) {
        DataSource dataSource = DataService.getDataSource();
        if(!(dataSource instanceof TableDishRelations))
            return;
        LinearLayout btnsParent = (LinearLayout)findViewById(R.id.order_btns);
        for(int i = btnsParent.getChildCount(); i > 0; i--) {
            Button btn = (Button)btnsParent.getChildAt(i-1);
            int tableNr = Integer.parseInt(btn.getText().toString().replace("Bord ", ""));
            if(tableOrders.getDishesFromTable(tableNr-1).size() > 0) {
                List<TableHasDish> tbl2 = tableOrders.getDishesFromTable(tableNr-1);
                List<TableHasDish> tbl1 = tableOrdersLocal.getDishesFromTable(tableNr-1);
                
                boolean isEqual = tbl1.size() == tbl2.size();
                if(isEqual) {
                    for(TableHasDish hd : tbl1) {
                        for(TableHasDish hd2 : tbl2) {
                            if(hd.getDish().id != hd2.getDish().id)
                                isEqual = false;
                        }
                    }
                }
                // om tableOrder == dataSource => lagar mat => orange
                if(isEqual)
                   btn.setBackgroundResource(R.drawable.orange_button);
                // annars => kund äter => grön
                else
                    btn.setBackgroundResource(R.drawable.green_button);
            }
            else
                btn.setBackgroundResource(R.drawable.default_button);
        }
    }

    private void updateTableOrderObject(DataSource dataSource) {
        Thread threadSync = new Thread(DataService.updateOnce);
        
        final DataSource tmpSrc = DataService.getDataSource();
        try {
            DataService.setDataSource(dataSource);
            threadSync.start();
            threadSync.join(SYNCDURATION_DELAY);
            if(threadSync.isAlive())
                threadSync.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(WaiterTableActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IllegalThreadStateException ex) {}
        finally {
            DataService.setDataSource(tmpSrc);
        }
    }
}
