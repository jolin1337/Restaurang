/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.website;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import se.miun.dt142g.Controller;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.EventPost;

/**
 * This class the event details and displays them on a jpanel in a form
 * application
 *
 * @author Johannes Lindén
 * @since 2014-10-11
 * @version 1.3
 */
public class EventPostPanel extends JPanel {

    /**
     * A variable to handle the eventpost
     */
    private EventPost eventPost = null;
    /**
     * Button to add a new image
     */
    private final JButton imgBtn = new JButton("Lägg till poster");
    /**
     * Textfield for the event date
     */
    private final JTextField editDate = new JTextField();
    /**
     * Textfield for the event title
     */
    private final JTextField editTitle = new JTextField();
    /**
     * Textarea for the event description
     */
    private final JTextArea editDesc = new JTextArea();
    /**
     * An instance of the controller to handle the tab-view
     */
    private final Controller remote;

    /**
     * New action listener for the imageEvent
     */
    private final ActionListener imageEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            final JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter(null, "jpg", "jpeg", "png");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(EventPostPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                eventPost.setImgSrc(file.getAbsolutePath());
                imgBtn.setText("Vald Poster: " + file.getName());
                remote.setSavedTab((JComponent) EventPostPanel.this.getParent(), false);
            }
        }
    };

    /**
     * Initiate components
     *
     * @param eventPost - The eventpost to initiate
     * @param c - The controller instance
     */
    public EventPostPanel(EventPost eventPost, Controller c) {
        this.eventPost = eventPost;
        remote = c;

        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Settings.Styles.darkBg);
        imgBtn.addActionListener(imageEvent);
        int index = eventPost.getImgSrc().lastIndexOf("/");
        if (index > -1) {
            imgBtn.setText(eventPost.getImgSrc().substring(index + 1));
        } else if (!eventPost.getImgSrc().isEmpty()) {
            imgBtn.setText(eventPost.getImgSrc());
        }
        add(imgBtn);

        editDate.setText(eventPost.getPubDate());
        editDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, editDate.getPreferredSize().height));
        editDate.addKeyListener(textFieldKeyListener);
        editDate.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Datum för evenemang:")));
        add(editDate);

        editTitle.setText(eventPost.getTitle());
        editTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, editTitle.getPreferredSize().height));
        editTitle.addKeyListener(textFieldKeyListener);
        editTitle.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Rubrik för evenemang:")));
        add(editTitle, BorderLayout.WEST);

        editDesc.setText(eventPost.getDescription());
        editDesc.addKeyListener(textFieldKeyListener);
        editDesc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.darkBg), BorderFactory.createTitledBorder("Beskrivning av enenemang:")));
        add(editDesc, BorderLayout.WEST);

    }

    /**
     * Keylistener to see whether something has been modified
     */
    KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent) EventPostPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };

    /**
     * Updates an event by setting the data to the data retrieved from the inout
     * fields
     */
    public void updateEvent() {
        eventPost.setDescription(editDesc.getText());
        eventPost.setPubDate(editDate.getText());
        eventPost.setTitle(editTitle.getText());
    }

    /**
     * Updates an eventpost by overriding it with a new eventpost
     *
     * @param e - Eventpost with the new data
     */
    public void setUpdatedEvent(EventPost e) {
        eventPost = e;
        updateEvent();
    }

    /**
     * Retrieves this eventpost
     *
     * @return this eventpost
     */
    public EventPost getEvent() {
        return eventPost;
    }
}
