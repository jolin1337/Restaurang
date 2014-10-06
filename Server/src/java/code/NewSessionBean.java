/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import data.NewEntity;
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
public class NewSessionBean {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    public List<NewEntity> getTexts() {
        TypedQuery<NewEntity> query = em.createNamedQuery("SelectNewEntity", NewEntity.class);
        return query.getResultList();

    }

    public void setText(String text) {
        NewEntity entity = new NewEntity();
        entity.setText(text);
        persist( entity );
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public void deleteRows(int count) {
        try {
            List<NewEntity> list = getTexts();
            for(int i=0;i<count;i++){
                
                System.out.println(list.get(i).getId());
                if (list.get(i).getId() != null) {
                    em.remove(list.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
