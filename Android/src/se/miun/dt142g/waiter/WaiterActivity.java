package se.miun.dt142g.waiter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import se.miun.dt142g.R;

public class WaiterActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_menu);
        LinearLayout parent = (LinearLayout)findViewById(R.id.order_btns);
        for(int i=0;i<1;i++) {
            
            Button btnTmp = new Button(this);
            btnTmp.setTextColor(Color.WHITE);
            btnTmp.setTextSize(30);
            btnTmp.layout(5, 5, 5, 5);
            btnTmp.setShadowLayer(2, 1, 1, Color.BLACK);
            btnTmp.setText("Bord " + (i+7));
            
            btnTmp.setBackgroundResource(R.drawable.default_button);
            parent.addView(btnTmp);
        }
    }
    public void table_click(View v) throws Exception {
        if(v.getClass() != Button.class)
            throw new Exception("No button click. The view passed is invalid");
        Button btn = (Button)v;
        Toast.makeText(getApplicationContext(),
                  String.valueOf(btn.getId()), Toast.LENGTH_LONG)
                  .show();
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
