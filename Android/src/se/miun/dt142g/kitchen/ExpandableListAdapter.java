package se.miun.dt142g.kitchen;


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import se.miun.dt142g.R;
import se.miun.dt142g.data.EntityRep.Dish;
import se.miun.dt142g.data.EntityRep.TableHasDish;
import se.miun.dt142g.data.EntityRep.TableOrder;
import se.miun.dt142g.data.entityhandler.DataService;
import se.miun.dt142g.data.entityhandler.DataSource;
import se.miun.dt142g.data.handler.TableDishRelations;
import se.miun.dt142g.data.handler.TableOrders;
/**
 * Created by Tomas on 2014-09-20.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    final private List<TableOrder> _listDataHeader; // header titles
    // child data in format of header title, child title
    final private HashMap<String, List<Dish>> _listDataChild;

    public ExpandableListAdapter(Context context, List<TableOrder> listDataHeader,
                                 HashMap<String, List<Dish>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        try {
            return this._listDataChild.get(getGroup(groupPosition))
                    .get(childPosition);
        } catch(IndexOutOfBoundsException ex) {
            return null;
        }catch(NullPointerException ex) {
            return null;
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Dish child = (Dish) getChild(groupPosition, childPosition);
        final String childText = child.getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.orderdetail, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this._listDataChild.get(getGroup(groupPosition)).size();
        }
        catch(IndexOutOfBoundsException ex) {
            return 0;
        }catch(NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public String getGroup(int groupPosition) {
        if(_listDataHeader != null)
            return "Bord " + (1+this._listDataHeader.get(groupPosition).getTable()) + "\n" + 
                    new SimpleDateFormat("HH:mm").format(this._listDataHeader.get(groupPosition).getTimeOfOrder());
        return null;
    }

    @Override
    public int getGroupCount() {
        if(_listDataHeader != null)
            return this._listDataHeader.size();
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.orders, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView klar = (ImageView) convertView.findViewById(R.id.button);
        klar.setFocusable(false);
        klar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something with database
                if(_listDataHeader != null) {
                    _listDataHeader.remove(groupPosition);
                    final DataSource ds = DataService.getDataSource();
                    synchronized(ds) {
                        if(ds instanceof TableDishRelations) {
                            try {
                                DataService.setAutoLoad(false);
                                ((TableDishRelations)ds).clearDishesFromTable(_listDataHeader.get(groupPosition).getTable());
                                DataService.updateServer();
                                DataService.setAutoLoad(true);
                            } catch(UnsupportedOperationException ex) {}
                            catch(IndexOutOfBoundsException ex) {}
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
}
