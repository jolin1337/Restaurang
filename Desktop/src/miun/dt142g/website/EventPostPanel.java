/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import miun.dt142g.data.EventPost;

/**
 *
 * @author Johannes
 */
public class EventPostPanel extends JPanel{
    private EventPost eventPost = null;

    private final JButton imgBtn = new JButton("Lägg till poster");
    private final TextField editDate = new TextField();
    private final TextField editTitle = new TextField();
    private final TextArea editDesc = new TextArea();
    
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
                imgBtn.setLabel("Vald Poster: " + file.getName());
            }
        }
    };
    public EventPostPanel(EventPost eventPost) {
        this.eventPost = eventPost;
        
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.LIGHT_GRAY);
        imgBtn.addActionListener(imageEvent);
        int index = eventPost.getImgSrc().lastIndexOf("/");
        if(index > -1)
            imgBtn.setLabel(eventPost.getImgSrc().substring(index+1));
        else if(!eventPost.getImgSrc().isEmpty())
            imgBtn.setLabel(eventPost.getImgSrc());
        add(imgBtn);
        
        JLabel date = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Datum för evenemang</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( date );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        editDate.setText(eventPost.getPubDate());
        editDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, editDate.getPreferredSize().height));
        add(editDate);
        
        
        JLabel lTitle = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Rubrik av evenemang</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( lTitle );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        editTitle.setText(eventPost.getTitle());
        editTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, editTitle.getPreferredSize().height));
        add(editTitle, BorderLayout.WEST);
        
        JLabel lDesc = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Beskrivning av evenemang</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( lDesc );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        editDesc.setText(eventPost.getDescription());
        add(editDesc, BorderLayout.WEST);
    }
    
}
