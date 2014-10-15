/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.reservations;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import static android.util.Log.i;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Reservation;
import se.miun.dt142g.data.EntityHandler.Reservations;

/**
 *
 * @author Ulf
 */
public class ReservationFragment extends Fragment {

    private Reservations reservations = new Reservations();
    
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";


    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

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

    public ReservationFragment() {
        reservations = new Reservations();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadFilesTask().execute("Hej");
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.reservations_fragment, container, false);

        
        
        // Set the title view to show the page number.
        Date theDay = new Date();
        theDay.setTime(theDay.getTime()+(mPageNumber)*(24*3600*1000));
        DateFormat theDate = new SimpleDateFormat("dd/MM-yy");
        
        String reserved=theDate.format(theDay)+"\n\n"; 
        
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
        return mPageNumber;
    }
     private class DownloadFilesTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Reservations re = new Reservations();
                re.dbConnect();
                /*
                URL url= new URL("http://10.0.2.2:8080/Server/");
                SAXParserFactory factory =SAXParserFactory.newInstance();
                SAXParser parser=factory.newSAXParser();
                //XMLReader xmlreader=parser.getXMLReader();
                //RssHandler theRSSHandler=new RssHandler();
                //xmlreader.setContentHandler(theRSSHandler);
                InputStream is=new InputSource(url.openStream()).getByteStream();
                int r;
                while((r = is.read()) != -1)
                    ;*/
                //xmlreader.parse(is);
                return 0;
            } catch (Exception e) {
                return 0;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
        }
    }
}
