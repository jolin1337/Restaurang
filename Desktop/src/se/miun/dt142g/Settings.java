/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g;

import java.awt.Color;

/**
 * This class has only static members. This is a configuration file for the
 * entire Desktop application.
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class Settings {

    /**
     * Some common used objects for styling
     */
    public static class Styles {

        /**
         * The background color for the entier application
         */
        public static Color applicationBgColor = new Color(245, 250, 255);
        /**
         * The background color for all JButtons
         */
        public static Color btnBackground = new Color(180, 0, 0);
        /**
         * The foreground color for all JButtons
         */
        public static Color btnForeground = applicationBgColor;
        /**
         * The background color for all JTextFields
         */
        public static Color fieldColor = new Color(255, 200, 200);
        /**
         * The background color to represent a group of components in a view
         */
        public static Color darkBg = new Color(225, 235, 235);
        /**
         * The date format to be used
         */
        public static String dateFormat = "dd/MM-yy 'kl:' HH:mm";
    }

    /**
     * Some common used strings
     */
    public static class Strings {

        /**
         * The logo source path
         */
        public static String logoSrc = "res/graphics/logo.png";
        /**
         * All the different titles in the tab menu
         */
        public static String[] tabTitles = {"Hem", "Rätter", "Hemsida", "Inventarie", "Användare", "A La Carté", "Veckans Meny", "Bokningar", "Schema"};
        /**
         * The server IP-address 
         */
        public static String serverURL = "http://localhost:8080/Server/";
        /**
         * The text on the submit button
         */
        public static String submit = "Spara ändringar";
        /**
         * Error message upon connection error towards the server
         */
        public static Object serverConnectionError = "There is an error in the authentication to the server or the server is down. Please check this out before do any changes!";
        /**
         * The error message when trying to add dishes to al a carté when no dishes available
         */
        public static Object noDishesCreatedError = "Inga rätter tillgängliga ännu. Du kommer att skickas till tabben rätter för att lägga till några sådanna";
    }

    /**
     * Default DishGroups values/names
     */
    public static String[] aLaCarte = new String[]{"A la Carte"};
    /**
     * The days of the week
     */
    public static String[] weekDays = new String[]{"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag"};
}
