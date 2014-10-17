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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
/**
 * 
 * @author Johannes
 */
public class WaiterActivity extends BaseActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_menu);
    }
    public void table_click(View v) throws Exception {
        if(v.getClass() != Button.class)
            throw new Exception("No button click. The view passed is invalid");
        Button btn = (Button)v;
        
        Intent ordersActivity = new Intent(getApplicationContext(), OrdersActivity.class);
        ordersActivity.putExtra("bord_str", btn.getText());
        ordersActivity.putExtra("bord_id", btn.getId());
        startActivityForResult(ordersActivity, 1337);
    }
    
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(resCode, resCode, data);
        if(resCode == 1337) {
            // To get data use
            // data.getExtras().get("data_name");
            Toast.makeText(getApplicationContext(),
                      "Result passed", Toast.LENGTH_LONG)
                      .show();
        }
    }
}
