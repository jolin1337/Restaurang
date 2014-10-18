/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.website;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import se.miun.dt142g.Controller;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.EventPost;

/**
 *
 * @author Johannes
 */
public class EventPostPanel extends JPanel {

    private EventPost eventPost = null;

    private final JButton imgBtn = new JButton("Lägg till poster");
    private final JTextField editDate = new JTextField();
    private final JTextField editTitle = new JTextField();
    private final JTextArea editDesc = new JTextArea();
    private final Controller remote;
    
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
                remote.setSavedTab((JComponent)EventPostPanel.this.getParent(), false);
            }
        }
    };

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
    
    KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            remote.setSavedTab((JComponent)EventPostPanel.this.getParent(), false);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };

    public void updateEvent() {
        eventPost.setDescription(editDesc.getText());
        eventPost.setPubDate(editDate.getText());
        eventPost.setTitle(editTitle.getText());
    }
    public void setUpdatedEvent(EventPost e) {
        eventPost = e;
        updateEvent();
    }
    public EventPost getEvent(){
        return eventPost;
    }
}
