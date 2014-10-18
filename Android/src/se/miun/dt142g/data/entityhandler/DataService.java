/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.entityhandler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.data.entityhandler.DataSourceListener;

/**
 *
 * @author Johannes
 */
public class DataService extends Service {
    private static final DataSourceListener background = new DataSourceListener();
    private final IBinder mBinder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
            throw new NullPointerException("Intent is null. We can't handle this right now in Service.");
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if(extras.getBoolean("ignoreResponse"))
                background.setShouldIgnoreDataResponse(true);
            int speed = intent.getExtras().getInt("syncSpeed", DataSourceListener.DEFAULT_SYNC_SPPED);
            background.setIntervallSpeed(speed);
        }
        startListener();
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            background.indicateStop();
            background.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(DataService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setSyncSpeed(int syncSpeed) {
        background.setIntervallSpeed(syncSpeed);
    }
    public static void shouldIgnoreResponse(boolean yesno) {
        background.setShouldIgnoreDataResponse(yesno);
    }
    /**
     * Very important to call in the service before starting this service
     * The ds is the data to sync between the server and this application
     * @param ds - The datasource to sync to the service
     */
    public static void setDataSource(DataSource ds) {
        background.setDataContainer(ds);
    }
    public static void setHandler(Handler h) {
        background.setHandler(h);
    }
    public static void startListener() {
        if(!background.isAlive())
            background.start();
    }
    public static boolean isAlive() {
        return background.isAlive();
    }
    public static DataSource getDataSource() {
        return background.getDataContainer();
    }
    public static void updateServer() {
        background.writeData();
    }
    
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        DataService getService() {
            return DataService.this;
        }
    }
}
