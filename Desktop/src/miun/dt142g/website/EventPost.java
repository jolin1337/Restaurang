/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.website;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Johannes
 */
public class EventPost extends JPanel implements ActionListener{
    private String imagePath;

    private final Button imgBtn = new Button("Lägg till poster");
    private final TextField editDate = new TextField();
    private final TextField editTitle = new TextField();
    private final TextArea editDesc = new TextArea();
    
    public EventPost() {
        setBorder(new EmptyBorder(10,10,10,10));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.LIGHT_GRAY);
        imgBtn.addActionListener(this);
        add(imgBtn);
        
        JLabel date = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Publicationsdatum</div></html>");
        Box  leftJustify = Box.createHorizontalBox();
        leftJustify.add( date );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        editDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, editDate.getPreferredSize().height));
        add(editDate);
        
        
        JLabel lTitle = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Rubrik av event</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( lTitle );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        editTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, editTitle.getPreferredSize().height));
        add(editTitle, BorderLayout.WEST);
        
        JLabel lDesc = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>Beskrivning av event</div></html>");
        leftJustify = Box.createHorizontalBox();
        leftJustify.add( lDesc );
        leftJustify.add( Box.createHorizontalGlue() );
        add(leftJustify);
        add(editDesc, BorderLayout.WEST);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        final JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter(null, "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            imgBtn.setLabel("Vald Poster: " + file.getName());
        }
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
}
