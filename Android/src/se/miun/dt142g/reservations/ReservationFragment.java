/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.reservations;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Reservation;
import se.miun.dt142g.data.handler.Reservations;
import se.miun.dt142g.data.handler.TableOrders;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.entityhandler.DataSourceListener;

/**
 *
 * @author Ulf
 */
public class ReservationFragment extends Fragment {

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                if(data.containsKey("connectionError")) {
                    // TODO: Print Toast message here
                }
                if(data.containsKey("dataUpdated") && data.getInt("dataUpdated") == DataSourceListener.UPDATE_CALL) {
                    updateViewText();
                }
            }

        }
        

    };
    DataSourceListener background = null;

    private final Reservations reservations = new Reservations();
    
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int fragmentNumber;
    private TextView textFieldInfo;
    
    public ReservationFragment() {
    }


    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     * @param pageNumber
     * @return 
     */
    public static ReservationFragment create(int pageNumber) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.reservations_fragment, container, false);

        textFieldInfo = ((TextView) rootView.findViewById(R.id.reservation));
        
        background = new DataSourceListener(reservations);
        background.setHandler(handler);
        background.start();
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     * @return The fragment amount of this fragment activity
     */
    public int getPageNumber() {
        return fragmentNumber;
    }
    private void updateViewText() {
        if(background == null) return;
        /* Take date from device and adapt it for the fragment to represent a
        different day of reservations in each fragment. */
        Date theDay = new Date();
        theDay.setTime(theDay.getTime()+(fragmentNumber)*(24*3600*1000));
        DateFormat theDate = new SimpleDateFormat("dd/MM-yy");
        String reserved=theDate.format(theDay)+"\n\n";
        for(Reservation r :  reservations.getReservations(theDay.getDate())){
            reserved += r.toString();
        }
        textFieldInfo.setText(reserved);
    }
}
