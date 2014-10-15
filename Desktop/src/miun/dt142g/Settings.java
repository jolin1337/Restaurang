/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g;

import java.awt.Color;

/**
 *
 * @author Johannes
 */
public class Settings {
    public static class Styles {
        public static Color btnBackground = new Color(180,0,0);
        public static Color btnForeground = Color.white;
        public static Color fieldColor = new Color(255,200,200);
        public static Color darkBg = new Color(240, 240, 240);
    }

    public static class Strings {
        public static String submit = "Synkronisera med server";
        public static Object serverConnectionError = "There is an error in the authentication to the server or the server is down. Please check this out before do any changes!";
    }
}
