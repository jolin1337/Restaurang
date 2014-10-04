/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.beans;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author jolin1337
 */
@Named
@Stateless
@Path("/{page}")
public class WebsiteMenuBar {

    @GET
    public String getPage(@PathParam("page") String page) {
        return page;
    }
}
