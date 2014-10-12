/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package data.entity;

import javax.json.JsonObject;
import javax.persistence.EntityManager;

/**
 * This class is the base of the entities for the server application It's purpos
 * is to demand definition of two very important methods
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
public abstract class JsonEntity {
    /**
     * Retrieves the PK name related to this entity
     * @return 
     */
    public static String getPK() {
        return "id";
    }
    
    /**
     * This function is used to set the data of an entity
     *
     * @param obj - The json object to parse to this entity
     * @param em - The entity manager for access on relations to current entity
     * @return True if this method succeed to save obj to the entity otherwise
     * false
     */
    public abstract boolean setEntityByJson(JsonObject obj, EntityManager em);

    /**
     * Converts the current entity to a string
     *
     * @return the string of a representation of this entity
     */
    public abstract String toJsonString();
}
