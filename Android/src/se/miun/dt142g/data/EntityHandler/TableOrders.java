/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.data.EntityHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.data.EntityRep.TableOrder;

/**
 *
 * @author Johannes
 */
public class TableOrders extends DataSource implements Iterable<TableOrder> {

    List<TableOrder> tableOrders;
    String table = "tableorder";

    public TableOrders() {
        tableOrders = new ArrayList<TableOrder>();
    }

    private void parseTable(String jsonStr) {
    }

    @Override
    public void loadData(String url, String responseText) throws WrongKeyException {
        if (url.equals("login")) {
            key = responseText;
        } else if (url.equals("gettable")) {
            parseTable(responseText);
        } else if(url.equals("updaterow")) {
            load();
        }
    }

    @Override
    public void load() throws WrongKeyException {
        String params = "key=" + key + "&table=" + table;
        String response = getRequest("gettable", params);
        System.out.println(response);
    }

    @Override
    public void update() throws WrongKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterator<TableOrder> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
