package se.miun.dt142g;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.login_activity);
    }
    
    public void loginButtonClicked(View v){
        EditText userName   = (EditText)findViewById(R.id.username);
        EditText passWord   = (EditText)findViewById(R.id.password);
        
        
        /**
         * Database stuff here
         */
        
        //Temporary activity change so we don't get stuck on the loginscreen
        Intent ordersActivity = new Intent(this, se.miun.dt142g.waiter.WaiterActivity.class);   
        startActivity(ordersActivity);
    }

}
