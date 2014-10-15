package se.miun.dt142g.kitchen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.DataEntityListener;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityHandler.TableOrders;
import se.miun.dt142g.data.EntityRep.TableOrder;


public class Orders extends BaseActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    TableOrders tableOrders = new TableOrders();

    DataEntityListener tableEntityListener = new DataEntityListener() {

        public void onUpdateTable() {
        }
        
        public void onReadTable() {
            prepareListData();
        
            //listAdapter = new ExpandableListAdapter(Orders.this, listDataHeader, listDataChild);
            // setting list adapter
            //expListView.setAdapter(listAdapter);
            if(listDataHeader.size() > 0)
                expListView.expandGroup(0);
            listAdapter.notifyDataSetChanged();
        }

        public void onFaildRequest(String req) {
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableOrders.setTableListener(tableEntityListener);
        setContentView(R.layout.activity_orders);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.ListOrders);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // preparing list data
        prepareListData();
        tableOrders.load();
        
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }

        });

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });


    }


    /*
        * Preparing the list data
        */
    private void prepareListData() {
        int index = 0;
        for(TableOrder tblOrder : tableOrders) {
            listDataHeader.add("Bord " + tblOrder.getId());

            // Adding child data
            List<String> bord1 = new ArrayList<String>();
            for(Integer dishIndex : tblOrder.getOrderedDishes()) {
                bord1.add("Rätt nr: " + dishIndex);
            }
            listDataChild.put(listDataHeader.get(index), bord1);
            index++;
        }
        /*
        // Adding child data
        listDataHeader.add("Bord 1");
        listDataHeader.add("Bord 2");
        listDataHeader.add("Bord 3");

        // Adding child data
        List<String> bord1 = new ArrayList<String>();
        bord1.add("Kyckling");
        bord1.add("Tonfisk");
        bord1.add("Lax");
        bord1.add("Biff");
        bord1.add("Soppa");

        List<String> bord2 = new ArrayList<String>();
        bord2.add("The Conjuring");
        bord2.add("Despicable Me 2 återkomsten: Gubbarna har tänkt klart");
        bord2.add("Turbo");
        bord2.add("Grown Ups 2");
        bord2.add("Red 2");
        bord2.add("The Wolverine");

        List<String> bord3 = new ArrayList<String>();
        bord3.add("2 Guns");
        bord3.add("The Smurfs 2");
        bord3.add("The Spectacular Now");
        bord3.add("The Canyons");
        bord3.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), bord1);
        listDataChild.put(listDataHeader.get(1), bord2);
        listDataChild.put(listDataHeader.get(2), bord3);
        */
    }

}
