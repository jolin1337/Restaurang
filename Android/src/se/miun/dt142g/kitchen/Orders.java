package se.miun.dt142g.kitchen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ExpandableListView;
import java.text.SimpleDateFormat;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;
import se.miun.dt142g.data.handler.TableOrders;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSourceListener;

public class Orders extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    final List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild;
    final TableOrders tableOrders = new TableOrders();

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

        DataService.setSyncSpeed(DataSourceListener.DEFAULT_SYNC_SPPED);
        DataService.setDataSource(tableOrders);
        DataService.setHandler(handler);
        DataService.setAutoLoad(true);
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
        synchronized(tableOrders) {
            int size = 0;
            for (TableOrder tblOrder : tableOrders) {

                // Adding child data
                List<Integer> tblDishes = tblOrder.getOrderedDishes();
                if(tblDishes.size() > 0) {
                    listDataHeader.add("Bord " + (tblOrder.getTable()+1) + "\n" + 
                            new SimpleDateFormat("HH:mm").format(tblOrder.getTimeOfOrder()));
                    List<String> bord1 = new ArrayList<String>();
                    for (Integer dishIndex : tblDishes) {
                        bord1.add(tableOrders.getDishes().getDish(dishIndex).getName());
                    }
                    listDataChild.put(listDataHeader.get(index), bord1);
                    index++;
                }
            }
        }
        if(listDataHeader.size() > 0)
            expListView.expandGroup(0);
        else
            prepareListData();
    }
}
