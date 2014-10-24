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
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.EntityRep.TableHasDish;
import se.miun.dt142g.data.handler.TableOrders;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSourceListener;
import se.miun.dt142g.data.entityhandler.TableDishRelations;

public class KitchenOrdersActivity extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    final List<TableOrder> listDataHeader = new ArrayList<TableOrder>();
    HashMap<String, List<Dish>> listDataChild;
    final TableDishRelations tableOrders = new TableDishRelations();

    // Define the Handler that receives messages from the thread and update the progress
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                if(data.containsKey("connectionError")) {
                    DataService.handleError(data.getInt("connectionError"));
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

        listDataChild = new HashMap<String, List<Dish>>();

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
        //listDataHeader.add("Ingen data tillgänglig på servern");
        //listDataChild.put(listDataHeader.get(0), null);

    }
    private void updateViewList() {
        listDataHeader.clear();
        listDataChild.clear();
        
        synchronized(tableOrders) {
            int size = tableOrders.getRelations().size();
            
            // Bubble sort table relations
            for (int index = 0; index < size; index++) {
                TableHasDish tblOrder = tableOrders.getRelations().get(index);
                for (int index2 = 0; index2 < size; index2++) {
                    TableHasDish tblOrder2 = tableOrders.getRelations().get(index2);
                    if(tblOrder2.getTableOrder().getTable() < tblOrder.getTableOrder().getTable()) {
                        tableOrders.getRelations().set(index2, tblOrder);
                        tableOrders.getRelations().set(index, tblOrder2);
                    }
                }
            }
            
            int prevTable = -1;
            List<Dish> table = null;
            int listIndex = 0;
            TableHasDish tblOrder  = null;
            for (int index = 0; index < size; index++) {
                tblOrder = tableOrders.getRelations().get(index);
                if(prevTable != tblOrder.getTableOrder().getTable()) {
                    if(table != null) {
                        listDataChild.put("Bord " + (1+listDataHeader.get(listIndex-1).getTable()) + "\n" + 
                            new SimpleDateFormat("HH:mm").format(listDataHeader.get(listIndex-1).getTimeOfOrder()), table);
                    }
                    listDataHeader.add(tblOrder.getTableOrder());
                    table = new ArrayList<Dish>();
                    listIndex++;

                }
                // table is never null lol, (removes warning)
                if(table != null) {
                    table.add(tableOrders.getDishes().getDish(tblOrder.getDish().id));
                }
                prevTable = tblOrder.getTableOrder().getTable();
            }
            if(tblOrder != null) {
                listDataChild.put("Bord " + (1+listDataHeader.get(listIndex-1).getTable()) + "\n" + 
                    new SimpleDateFormat("HH:mm").format(listDataHeader.get(listIndex-1).getTimeOfOrder()), table);
            }
        }
        if(listDataHeader.size() > 0)
            expListView.expandGroup(0);
        else
            prepareListData();
    }
}
