/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package code.beans;

import data.Settings;
import data.entity.Event;
import java.util.Date;
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
public class EventSessionBean {
    public enum SHOW_EVENTS  {
        SHOW_ALL,
        SHOW_BEFORE,
        SHOW_AFTER
    };
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;
    
   /**
    * @return List of events from the database
    */
    
    public List<Event> getEvents(SHOW_EVENTS toShow) {
        TypedQuery<Event> query;
        //gets all events
        if(toShow == SHOW_EVENTS.SHOW_ALL) 
            query = em.createNamedQuery("Event.findAll", Event.class);
        //gets evens after a given date
        else if( toShow == SHOW_EVENTS.SHOW_AFTER) {
            query = em.createNamedQuery("Event.findByPubdateAfter", Event.class);
            query.setParameter("pubdate", new Date());
        }
        //gets evens before a given date
        else if( toShow == SHOW_EVENTS.SHOW_BEFORE) {
            query = em.createNamedQuery("Event.findByPubdateBefore", Event.class);
            query.setParameter("pubdate", new Date());
        }
        else return null;
        return query.getResultList();
    }

    public SHOW_EVENTS getShowAll() {
        return SHOW_EVENTS.SHOW_ALL;
    }
    public SHOW_EVENTS getShowBefore() {
        return SHOW_EVENTS.SHOW_BEFORE;
    }
    public SHOW_EVENTS getShowAfter() {
        return SHOW_EVENTS.SHOW_AFTER;
    }
    
    public String getUploadUrl() {
        return Settings.imagePath;//.replace("/", "\\");
    }
}
