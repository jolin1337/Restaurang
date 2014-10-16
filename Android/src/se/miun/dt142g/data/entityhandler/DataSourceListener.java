/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.entityhandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class listens on changes of the data and works as a thread for syncing 
 * to database
 *
 * @author Johannes Lindén
 * @since 2014-10-09
 * @version 1.2
 */
public class DataSourceListener extends Thread {
    public static final int CONNECTION_ERROR = 1;
    public static final int UPDATE_CALL = 2;
    public static final int FAST_SYNC_SPPED = 2000;
    public static final int SLOW_SYNC_SPPED = 10000;
    public static final int DEFAULT_SYNC_SPPED = 5000;
    /**
     * The container of the data to sync to database
     */
    protected DataSource dataContainer = null;
    /**
     * The thread objects handler for syncing between Androidview and datasources
     * This object specifies the caller
     */
    protected Handler handler;
    /**
     * No connection or we are unable to talk to the correct server
     */
    private Boolean connectionErrorToSend = null;
    /**
     * Secounds between each sync
     */
    private int intervallSpeed = DEFAULT_SYNC_SPPED;
    /**
     * Indicates if the threadloop should stop or continue looping
     */
    private boolean shouldRun = false;
    /**
     * Indicates if we shoudl write the active state of dataContainer to database
     */
    private boolean shouldWrite = false;
    /**
     * Indicates if we should ignore the handle or not
     */
    private boolean shouldIgnoreDataResponse = false;
    
    /**
     * @param ds - The datasource to use
     * @param syncSpeed - The speed of the synctonization process (the lower the
     * faster)
     * @throws NullPointerException - If the datasource object has not been set
     * (you can do this one way by calling "setDataContainer")
     */
    public DataSourceListener(DataSource ds, int syncSpeed) throws NullPointerException {
        this(ds);
        if(syncSpeed < DEFAULT_SYNC_SPPED)
            intervallSpeed = FAST_SYNC_SPPED;
        else if(syncSpeed < SLOW_SYNC_SPPED)
            intervallSpeed = DEFAULT_SYNC_SPPED;
        else
            intervallSpeed = SLOW_SYNC_SPPED;
        
    }
    
    /**
     * @param ds - The datasource to use
     * 
     * @throws NullPointerException - If the datasource object has not been set
     * (you can do this one way by calling "setDataContainer")
     */
    public DataSourceListener(DataSource ds) throws NullPointerException {
        super();
        if(ds != null) 
            dataContainer = ds;
        else throw new NullPointerException("You have not set the DataSource object, do this by calling \"setDataContainer\"");
    }
    public DataSourceListener() {
        super();
    }

    @Override
    public void run() {
        if(connectionErrorToSend == null) {
            try {
                dataContainer.dbConnect();
                dataContainer.loadData();
                connectionErrorToSend = false;
                shouldRun = true;
            } catch (DataSource.WrongKeyException ex) {
                connectionErrorToSend = true;
            }
        }
        while(isRunning()) {
            try {
                if(shouldWrite) {
                    intervallSpeed = FAST_SYNC_SPPED;
                    dataContainer.update();
                }
                else {
                    intervallSpeed = DEFAULT_SYNC_SPPED;
                    dataContainer.loadData();
                }
                if(connectionErrorToSend)
                    sendCode("connectionError", CONNECTION_ERROR);
                else
                    sendCode("dataUpdated", UPDATE_CALL);
                try {
                    Thread.sleep(intervallSpeed);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DataSourceListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DataSource.WrongKeyException ex) {
                Logger.getLogger(DataSourceListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void sendMessage(String msg) {
        if(handler == null && !shouldIgnoreDataResponse) 
            throw new NullPointerException("No handler set to "
                + "send information to, please use setHandler for this purpous.");
        else if(shouldIgnoreDataResponse) return;
        Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("message", msg);
        msgObj.setData(b);
        handler.sendMessage(msgObj);
    }
    private void sendCode(String key, int code) {
        if(handler == null && !shouldIgnoreDataResponse) 
            throw new NullPointerException("No handler set to "
                + "send information to, please use setHandler for this purpous.");
        else if(shouldIgnoreDataResponse) return;
        Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(key, code);
        msgObj.setData(b);
        handler.sendMessage(msgObj);
    }
    
    /**
     * Sets the datasource to controll/sync
     * @param dc - The datasource to manage
     */
    public void setDataContainer(DataSource dc) {
        dataContainer = dc;
    }
    /**
     * Gets the datasource object
     * @return the datasource of this listener
     */
    public DataSource getDataContainer() {
        return dataContainer;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getIntervallSpeed() {
        return intervallSpeed;
    }

    public void setIntervallSpeed(int intervallSpeed) {
        this.intervallSpeed = intervallSpeed;
    }

    public boolean isRunning() {
        return shouldRun;
    }
    public void indicateStop() {
        shouldRun = false;
    }
    public void writeData() {
        shouldWrite = true;
    }
    /**
     * Indicates if the listener should sync to databse (false) or not (true)
     * @return True if this listener has information to write to database 
     * otherwise false
     */
    public boolean hasWriten() {
        return !shouldWrite;
    }

    public boolean shouldIgnoreDataResponse() {
        return shouldIgnoreDataResponse;
    }

    public void setShouldIgnoreDataResponse(boolean shouldIgnoreDataResponse) {
        this.shouldIgnoreDataResponse = shouldIgnoreDataResponse;
    }
}
