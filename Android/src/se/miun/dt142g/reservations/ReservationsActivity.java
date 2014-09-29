/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.reservations;

import android.os.Bundle;
import android.widget.TextView;
import se.miun.dt142g.BaseActivity;
import se.miun.dt142g.R;


/**
 *
 * @author Ulf
 */
public class ReservationsActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     * @param icicle The bundle for current activity
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.reservations);
        TextView reservations = (TextView) findViewById(R.id.reservationsText);
        reservations.setText("19-21: Pettersson");
        reservations.append("\n20-22: Löfvén");
    }
}

