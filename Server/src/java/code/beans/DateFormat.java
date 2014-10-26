/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Named;

/**
 *
 * @author Johannes
 */
@Stateful
@Named
public class DateFormat {
    
    public String[] getMonths() {
        final String[] resArray = new String[] {
            "Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", 
            "Augusti", "September", "Oktober", "November", "December"
        };
        List<String> res = new ArrayList<String>();
        
        return resArray;
    }
    
    public List<Integer> getIntegerItems(int count) {
        List<Integer> res = new ArrayList<>();
        for(int i=0; i < count; i++)
            res.add(i+1);
        return res;
    }

    
}
