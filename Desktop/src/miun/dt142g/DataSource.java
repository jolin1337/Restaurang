/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g;

/**
 *
 * @author Johannes
 */
public abstract class DataSource {
    // TODO: Implement loadData funktion in each inherited class
    public abstract void loadData();
    
    // TODO: create db connection
    public void dbConnect() {
        // Do connection here
    }
    protected void upploadData(String... data) {
        // TODO: Update data
    }
    public abstract void update();
}

