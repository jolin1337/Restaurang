/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package miun.dt142g.pdf;

import data.entity.Dish;
import data.entity.Dishgroup;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * This class exports all dishes to a pdf
 *
 * @author Johannes Lindén
 * @since 2014-10-07
 * @version 1.0
 */
public class PDFGenreateDishesMenu {

    public static void generatePDFTo(OutputStream out, String logoPath, List<Dishgroup> dishGroup) {
        PDDocument doc = null;
        try {
            doc = new PDDocument();

            PDPage page = new PDPage();
            doc.addPage(page);
            PDFont font = PDType1Font.COURIER_BOLD_OBLIQUE;
            Color color = new Color(200, 10, 10);

            PDPageContentStream contentStream;
            try {
                contentStream = new PDPageContentStream(doc, page);
                // TODO: Generate image o pdf
                //PDJpeg img = new PDJpeg(doc, new FileInputStream(logoPath));
                //contentStream.drawImage(img, 100, 700);
                contentStream.beginText();
                contentStream.setFont(font, 33);
                contentStream.setNonStrokingColor(color);
                contentStream.moveTextPositionByAmount(100, 700);
                contentStream.drawString("Meny");
                contentStream.endText(); 
                int i = 0;
                for (Dishgroup dgroup : dishGroup) {
                    contentStream.beginText();
                    contentStream.setFont(font, 22);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.moveTextPositionByAmount(100, 670-i*15);
                    contentStream.drawString(dgroup.getName());
                    contentStream.endText();
                    i+=2;
                    for(Dish d : dgroup.getDishList()) {
                        contentStream.beginText();
                        contentStream.setFont(font, 14);
                        contentStream.setNonStrokingColor(Color.DARK_GRAY);
                        contentStream.moveTextPositionByAmount(100, 670-i*15);
                        contentStream.drawString(d.getName()+": "+d.getFormattedPrice());
                        contentStream.endText();
                        i++;
                    }
                    i++;
                }
                contentStream.close();
                try {
                    doc.save(out);
                } catch (COSVisitorException ex) {
                    Logger.getLogger(PDFGenreateDishesMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(PDFGenreateDishesMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException ex) {
                    Logger.getLogger(PDFGenreateDishesMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } /*
         try {
         // Create a document and add a page to it
         PDDocument document = new PDDocument();
         PDPage page = new PDPage();
         document.addPage( page );
            
         // Create a new font object selecting one of the PDF base fonts
         PDFont font = PDType1Font.HELVETICA_BOLD;
            
         // Start a new content stream which will "hold" the to be created content
         PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
         // Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
         contentStream.beginText();
         contentStream.setFont( font, 12 );
         contentStream.moveTextPositionByAmount( 0, 0 );
         contentStream.drawString( "Hello World" );
         contentStream.endText();
            
         // Make sure that the content stream is closed:
         contentStream.close();
            
         try {
         // Save the results and ensure that the document is properly closed:
         document.save( out );
         } catch (COSVisitorException ex) {
         Logger.getLogger(PDFGenreateDishesMenu.class.getName()).log(Level.SEVERE, null, ex);
         }
         document.close();
         } catch (IOException ex) {
         Logger.getLogger(PDFGenreateDishesMenu.class.getName()).log(Level.SEVERE, null, ex);
         }*/

    }
}
