package se.miun.dt142g.kitchen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.handler.TableOrders;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataSourceListener;

public class Orders extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    final List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild;
    TableOrders tableOrders = new TableOrders();

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
                    updateViewList();
                    listAdapter.notifyDataSetChanged();
                }
            }

        }
        

    };
    DataSourceListener background = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tableOrders.setTableListener(tableEntityListener);
        setContentView(R.layout.activity_orders);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.ListOrders);

        listDataChild = new HashMap<String, List<String>>();

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);

        background = new DataSourceListener(tableOrders);
        background.setHandler(handler);
        background.start();
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader.add("Ingen data tillgänglig på servern");
        listDataChild.put(listDataHeader.get(0), null);

    }
    private void updateViewList() {
        listDataHeader.clear();
        listDataChild.clear();
        int index = 0;
        for (TableOrder tblOrder : tableOrders) {
            listDataHeader.add("Bord " + tblOrder.getId());

            // Adding child data
            List<String> bord1 = new ArrayList<String>();
            for (Integer dishIndex : tblOrder.getOrderedDishes()) {
                bord1.add("Rätt nr: " + dishIndex);
            }
            listDataChild.put(listDataHeader.get(index), bord1);
            index++;
        }
        
        if(listDataHeader.size() > 0)
            expListView.expandGroup(0);
    }

    @Override
    protected void onDestroy() {
        if (background != null) {
            background.indicateStop();
        }
    }
}
