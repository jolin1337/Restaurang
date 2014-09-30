/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.data;

import miun.dt142g.DataSource;

/**
 *
 * @author Johannes
 */
public class AboutUs extends DataSource {
    private final String whatOpen = "openhours";
    private final String whatContacts = "contactinfo";
    private String dataOpen="";
    private String dataContacts="";
    
    public AboutUs() { 
    }

    public String getDataOpen() {
        return dataOpen;
    }

    public void setDataOpen(String dataOpen) {
        this.dataOpen = dataOpen;
    }

    public String getDataContacts() {
        return dataContacts;
    }

    public void setDataContacts(String dataContacts) {
        this.dataContacts = dataContacts;
    }
    
    @Override
    public void loadData() {
        dataOpen = "Öppetider är...";
        dataContacts = "Du når oss på...";
    }

    @Override
    public void update() {
    }
    
}
