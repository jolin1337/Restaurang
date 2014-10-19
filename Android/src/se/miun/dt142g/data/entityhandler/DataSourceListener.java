/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.entityhandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.waiter.WaiterTableActivity;


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
    private final Boolean dataContainerDummy = true;
    /**
     * The thread objects handler for syncing between Androidview and datasources
     * This object specifies the caller
     */
    protected Handler handler;
    private final Boolean handlerDummy = true;
    /**
     * No connection or we are unable to talk to the correct server
     */
    private Boolean connectionErrorToSend = null;
    /**
     * Secounds between each sync
     */
    private final AtomicInteger intervallSpeed = new AtomicInteger(DEFAULT_SYNC_SPPED);
    /**
     * Indicates if the threadloop should stop or continue looping
     */
    private final AtomicBoolean shouldRun = new AtomicBoolean(false);
    /**
     * Indicates if we shoudl write the active state of dataContainer to database
     */
    private final AtomicBoolean shouldWrite = new AtomicBoolean(false);
    /**
     * Indicates if we should ignore the handle or not
     */
    private final AtomicBoolean shouldIgnoreDataResponse = new AtomicBoolean(false);
    /**
     * Indicates if we should download server data or not
     */
    private final AtomicBoolean autoLoadData = new AtomicBoolean(true);
    
    
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
            intervallSpeed.lazySet(FAST_SYNC_SPPED);
        else if(syncSpeed < SLOW_SYNC_SPPED)
            intervallSpeed.lazySet(DEFAULT_SYNC_SPPED);
        else
            intervallSpeed.lazySet(SLOW_SYNC_SPPED);
        
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
                synchronized(dataContainerDummy) {
                    dataContainer.dbConnect();
                    dataContainer.loadData();
                }
                connectionErrorToSend = false;
                shouldRun.compareAndSet(false, true);
            } catch (DataSource.WrongKeyException ex) {
                connectionErrorToSend = true;
            } catch (NullPointerException ex) {
                connectionErrorToSend = true;
            }
        }
        while(isRunning()) {
            try {
                if(shouldWrite.get()) {
                    intervallSpeed.set(FAST_SYNC_SPPED);
                    synchronized(dataContainerDummy) {
                        dataContainer.update();
                    }
                    shouldWrite.set(false);
                }
                else if(autoLoadData.get()) {
                    intervallSpeed.set(DEFAULT_SYNC_SPPED);
                    synchronized(dataContainerDummy) {
                        dataContainer.loadData();
                    }
                }
                if(connectionErrorToSend)
                    sendCode("connectionError", CONNECTION_ERROR);
                else
                    sendCode("dataUpdated", UPDATE_CALL);
                try {
                    Thread.sleep(intervallSpeed.get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(DataSourceListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DataSource.WrongKeyException ex) {
                Logger.getLogger(DataSourceListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void sendMessage(String msg) {
        Message msgObj;
        synchronized(handlerDummy) {
            if(handler == null && !shouldIgnoreDataResponse.get()) 
                throw new NullPointerException("No handler set to "
                    + "send information to, please use setHandler for this purpous.");
            else if(shouldIgnoreDataResponse.get()) return;
            msgObj = handler.obtainMessage();
        }
        Bundle b = new Bundle();
        b.putString("message", msg);
        msgObj.setData(b);
        synchronized(handlerDummy) {
            handler.sendMessage(msgObj);
        }
    }
    private void sendCode(String key, int code) {
        Message msgObj;
        synchronized(handlerDummy) {
            if(handler == null && !shouldIgnoreDataResponse.get()) 
                throw new NullPointerException("No handler set to "
                    + "send information to, please use setHandler for this purpous.");
            else if(shouldIgnoreDataResponse.get()) return;
            msgObj = handler.obtainMessage();
        }
        Bundle b = new Bundle();
        b.putInt(key, code);
        msgObj.setData(b);
        synchronized(handlerDummy) {
            handler.sendMessage(msgObj);
        }
    }
    
    /**
     * Sets the datasource to controll/sync
     * @param dc - The datasource to manage
     */
    public void setDataContainer(DataSource dc) {
        System.out.println("Changed datasource to: " + dc.toString());
        synchronized(dataContainerDummy) {
            dataContainer = dc;
        }
    }
    /**
     * Gets the datasource object
     * @return the datasource of this listener
     */
    public DataSource getDataContainer() {
        synchronized(dataContainerDummy) {
            return dataContainer;
        }
    }

    public Handler getHandler() {
        synchronized(handlerDummy) {
            return handler;
        }
    }

    public void setHandler(Handler handler) {
        synchronized(handlerDummy) {
            this.handler = handler;
        }
    }

    public int getIntervallSpeed() {
        return intervallSpeed.get();
    }

    public void setIntervallSpeed(int intervallSpeed) {
        this.intervallSpeed.lazySet(intervallSpeed);
    }

    public boolean isRunning() {
        return shouldRun.get();
    }
    public void indicateStop() {
        shouldRun.set(false);
    }
    public void writeData() {
        shouldWrite.set(true);
    }
    /**
     * Indicates if the listener should sync to databse (false) or not (true)
     * @return True if this listener has information to write to database 
     * otherwise false
     */
    public boolean hasWriten() {
        return !shouldWrite.get();
    }

    public boolean shouldIgnoreDataResponse() {
        return shouldIgnoreDataResponse.get();
    }

    public void setShouldIgnoreDataResponse(boolean shouldIgnoreDataResponse) {
        this.shouldIgnoreDataResponse.lazySet(shouldIgnoreDataResponse);
    }

    void setAutoLoadData(boolean b) {
        autoLoadData.set(b);
    }
}
