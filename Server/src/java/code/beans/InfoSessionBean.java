/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author jolin1337
 */
@Named
@Stateful
public class InfoSessionBean {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    private String getInfo(String what) {
        try {
            TypedQuery<Info> query = em.createNamedQuery("Info.findByWhat", Info.class);
            query.setParameter("what", what);
            List<Info> res = query.getResultList();
            if(res.size() > 0)
                return res.get(0).getDataInformation();
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
