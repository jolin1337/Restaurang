package se.miun.dt142g.bookings;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.data.Booking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Holds a list of all existing bookings
 *
 * @author Marcus
 */
public class Bookings extends DataSource implements Iterable<Booking> {

    /**
     * The list of reservations
     */
    private final List<Booking> bookings = new ArrayList<>();
    /**
     * The table name to syncronize to the server
     */
    private final String table = "booking";

    public Bookings() {
    }

    /**
     * Retrieves the amount of entries in bookings
     *
     * @return type is integer
     */
    public int getRows() {
        return bookings.size();
    }

    /**
     * Returns the requested booking if it exists.
     *
     * @param id of the requested booking
     * @return the booking instance or null if it doesn't exist.
     */
    public Booking getBooking(int id) {
        for (Booking b : bookings) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    /**
     * Returns the requested booking if it exists by index
     *
     * @param index of the requested booking
     * @return the booking instance
     */
    public Booking getBookingByIndex(int index) {
        return bookings.get(index);
    }

    /**
     * Add a booking into bookings
     *
     * @param booking to be added
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Remove a booking by id locally and remotely from the database
     *
     * @param id of the booking to be removed
     */
    public void removeBooking(int id) {
        bookings.remove(this.getBooking(id));
        removeBookingDb(id);
    }

    /**
     * Updates an existing booking reffered by id
     *
     * @param id of the booking up be updated
     * @param booking the new booking data to be added
     */
    public void editBooking(int id, Booking booking) {
        bookings.set(bookings.indexOf(id), booking);
    }

    @Override
    public void loadData() throws WrongKeyException {
        List<Booking> bks = getDataList();
        bookings.clear();
        for (Booking booking : bks) {
            bookings.add(booking);
        }
    }

    /**
     * Attempts to create a new booking instance
     *
     * @param bok the booking to be added.
     */
    private Booking getJsonBooking(JSONObject bok) {
        try {
            Booking b = new Booking(bok.getInt("id"), bok.getString("name"), new Date(bok.getLong("date")), bok.getInt("duration"), bok.getInt("persons"), bok.getString("phone"));
            return b;
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void update() throws WrongKeyException {
        List<Booking> bks = getDataList();

        String strRm = "&table=" + table + "&data={\"data\":[";
        String str = "&table=" + table + "&data=" + toJsonString(true);
        for (Booking booking : bks) {
            strRm += "{\"data\":{\"remove\":true,\"id\":" + booking.getId() + "}},";
        }

        if (strRm.charAt(strRm.length() - 1) == ',') {
            strRm = strRm.substring(0, strRm.length() - 1);
        }

        // System.out.println(str.substring(0, str.length()-1) + "]}");
        // System.out.println(strRm.substring(0, strRm.length()-1) + "]}");
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + strRm + "]}"));
        System.out.println("Updatestatus: " + getRequest("updaterow", "key=" + key + str));
        loadData();
    }

    @Override
    public int getUniqueId() {
        int id = -1;
        for (Booking bok : bookings) {
            if (bok.getId() <= id) {
                id = bok.getId() - 1;
            }
        }
        return id;
    }

    @Override
    public Iterator<Booking> iterator() {
        return bookings.iterator();
    }

    /**
     * Removes an instance of bookings in the databse based on specified ID
     *
     * @param id is the ID of the booking instance to be removed
     * @return returns whether the instance has successfully been remoeved or
     * not
     */
    private boolean removeBookingDb(int id) {
        JSONObject objectToRemove = new JSONObject();
        JSONObject jsonData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject containerToSend = new JSONObject();
        try {
            objectToRemove.put("id", id);
            objectToRemove.put("remove", true);
            jsonData.put("data", objectToRemove);
            jsonArray.put(jsonData);
            containerToSend.put("data", jsonArray);
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        String params = "key=" + key + "&table=" + table + "&data=" + containerToSend.toString();
        System.out.println("Update status: " + getRequest("updaterow", params));
        return true;
    }

    /**
     * Add a booking instance to the database
     *
     * @param b is the booking to be added
     */
    public void addBookingDb(Booking b) {
        String str = "key=" + key + "&table=" + table + "&data={\"data\":[" + b.toJsonString() + "]}";
        System.out.println("Update status: " + getRequest("updaterow", str));
    }

    /**
     * Converts this object to a json string
     *
     * @param newId if true create a new booking
     * @return The json string representing this object
     */
    public String toJsonString(boolean newId) {
        String str = "{\"data\":[";
        JSONArray data = new JSONArray();
        for (Booking booking : bookings) {
            int id = booking.getId();
            if (newId) {
                booking.setId(-1);
            }
            str += "{\"data\":" + booking.toJsonString() + "},";
            if (newId) {
                booking.setId(id);
            }
        }
        if (str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str + "]}";
    }

    /**
     * Retrieves data from the database
     *
     * @return a list of all data (booking objects)
     * @throws se.miun.dt142g.DataSource.WrongKeyException
     */
    private List<Booking> getDataList() throws WrongKeyException {
        List<Booking> res = new ArrayList<>();
        JSONObject response = null;
        JSONArray data = null;
        try {
            response = getJsonRequest(table);
            data = response.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < data.length(); i++) {
            try {
                Booking b = getJsonBooking(data.getJSONObject(i));
                if (b != null) {
                    res.add(b);
                }
            } catch (JSONException ex) {
                Logger.getLogger(Bookings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }
}
