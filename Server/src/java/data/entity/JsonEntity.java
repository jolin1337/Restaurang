/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.entity;

import javax.json.JsonObject;
import javax.persistence.EntityManager;

/**
 *
 * @author Johannes
 */
public abstract class JsonEntity {
    public abstract boolean setEntityByJson(JsonObject obj, EntityManager em);
    public abstract String toJsonString();
}
