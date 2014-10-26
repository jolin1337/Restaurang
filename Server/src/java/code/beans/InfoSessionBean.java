/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.beans;

import data.entity.Info;
import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
 

/**
 * This class gets information about contact and opening hours
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Named
@Stateful
public class InfoSessionBean {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;
    
    
   /**
    *@param Row in the INFO table to get data from.
    *@return data for the row. 
    */
    
    private String getInfo(String what) {
        try {
            TypedQuery<Info> query = em.createNamedQuery("Info.findByWhat", Info.class);
            query.setParameter("what", what);
            List<Info> res = query.getResultList();
            if(res.size() > 0) {
                String resStr = res.get(0).getDataInformation();
                resStr = resStr.replace("\n", "\\n").replace("\"", "'");
                return resStr;
            }
        }
        catch(javax.persistence.NoResultException e) {}
        //catch(Exception e) {}
        return "No data found...";
    }
    public String getOpenings() {
        return getInfo("openings");
    }
    public String getContactInfo() {
        return getInfo("contacts");
    }
}
