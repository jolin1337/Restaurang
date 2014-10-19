/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.beans;

import data.entity.Dishgroup;
import data.entity.Event;
import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


/**
 * This class handles get requests of events displaying on the page
 * @TODO Fix loggin of logins to the database
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
@Named
@Stateful
public class DishGroupSessionBean {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    public List<Dishgroup> getAlaCarte() {
        TypedQuery<Dishgroup> query = em.createNamedQuery("Dishgroup.alaCarte", Dishgroup.class);
        return query.getResultList();
    }
    
    public List<Dishgroup> getWeekMenu() {
        TypedQuery<Dishgroup> query = em.createNamedQuery("Dishgroup.weekMenu", Dishgroup.class);
        List<Dishgroup> dishgroups = query.getResultList();
        Dishgroup.sortDishgroups(dishgroups);
        return dishgroups;
    }
    
    
}
