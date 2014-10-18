package se.miun.dt142g;

import se.miun.dt142g.data.entityhandler.DataService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
//import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.handler.Users;

public class LoginActivity extends Activity {

    EditText userName = null;
    EditText userPwd = null;

//    DataSourceListener background = null;
    final Users users = new Users();
    Intent dataIntent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userName = (EditText) findViewById(R.id.username);
        userPwd = (EditText) findViewById(R.id.password);

//        background = new DataSourceListener(users);
//        background.setShouldIgnoreDataResponse(true);
//        background.setIntervallSpeed(DataSourceListener.SLOW_SYNC_SPPED);
//        background.start();
        dataIntent = new Intent(getBaseContext(), DataService.class);
        DataService.setSyncSpeed(DataSourceListener.SLOW_SYNC_SPPED);
        DataService.setDataSource(users);
        DataService.setHandler(handler);
        
        startService(dataIntent);
    }
    Handler handler = new Handler();

    public void loginButtonClicked(View v) {
        if(userName == null || userPwd == null) return;

        // find in database over here:
        synchronized(users) {
            if (users.findUserWithCredentials(userName.getText().toString(), userPwd.getText().toString())) {

                //Temporary activity change so we don't get stuck on the loginscreen
                Intent ordersActivity = new Intent(this, se.miun.dt142g.waiter.WaiterActivity.class);
                startActivity(ordersActivity);
            } else {
                Toast.makeText(this, 
                        "Ledsen men du matade in fel användarnamn eller lösenord.", 
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(dataIntent);

    }
}
