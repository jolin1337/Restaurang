/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.reservations;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Reservation;
import se.miun.dt142g.data.EntityHandler.Reservations;

/**
 *
 * @author Ulf
 */
public class ReservationFragment extends Fragment {
    
    

    private static Reservations reservations;
    
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int fragmentNumber;
    
    public ReservationFragment() {
    }


    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
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

        /* Take date from device and adapt it for the fragment to represent a 
        different day of reservations in each fragment. */
        Date theDay = new Date();
        theDay.setTime(theDay.getTime()+(fragmentNumber)*(24*3600*1000));
        DateFormat theDate = new SimpleDateFormat("dd/MM-yy");
        
        String reserved=theDate.format(theDay)+"\n\n"; 
        Reservations reservations = new Reservations(); 
        reservations.load();
        for(Reservation r :  reservations.getReservations(theDay.getDate())){
            reserved+=r.toString();
        }
        ((TextView) rootView.findViewById(R.id.reservation)).setText(reserved);
        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return fragmentNumber;
    }
}
