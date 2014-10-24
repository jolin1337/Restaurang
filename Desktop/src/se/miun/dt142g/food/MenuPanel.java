package se.miun.dt142g.food;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Dish;

/**
 *
 * @author Tomas
 */
public class MenuPanel extends JPanel {

    static DishGroups dishGroups = new DishGroups();
    JButton addDishBtn;
    private Controller remote = null;
    List<SingleDishPanel> dishPanel = new ArrayList<>();
    String[] activeDishGroups;

    private final ActionListener syncGroupEvent = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                List<String> takenGroups = new ArrayList<>();
                for(SingleDishPanel sdp : dishPanel) {
                    if(!takenGroups.contains(sdp.getGroupName())) {
                        dishGroups.removeAllFromGroup(sdp.getGroupName());
                        takenGroups.add(sdp.getGroupName());
                    }
                    //for(int i = sdp.myComboBox.getItemCount(); i > 0; i--) {
                    dishGroups.addDishToGroup(sdp.getGroupName(), (Dish)sdp.myComboBox.getSelectedItem());
                    //}
                }
                dishGroups.update();
            } catch (DataSource.WrongKeyException ex) {
                JOptionPane.showMessageDialog(MenuPanel.this,
                    Settings.Strings.serverConnectionError,
                    "Server error",
                    JOptionPane.ERROR_MESSAGE);
                if(remote != null)
                    remote.setConnectionView();
            }
        }
    };
    
    public MenuPanel(Controller c, String[] groupNames) throws DataSource.WrongKeyException {
        this.remote = c;
        dishGroups.dbConnect();
        dishGroups.loadData();
        
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Settings.Styles.applicationBgColor);
        
        JButton printButton = new JButton("Skriv ut menyblad");
        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printDishesPDF();
            }
        });
        add(printButton);
        
        JButton submitBtn = new JButton(Settings.Strings.submit);
        submitBtn.addActionListener(syncGroupEvent);
        add(submitBtn);
        List<DishGroup> dishList = dishGroups.getDishGroups(groupNames);
        
        for (final DishGroup dishGroup : dishList) {
            
            JLabel groupTitle = new JLabel(dishGroup.getGroup());
            add(groupTitle);
            
            JPanel groupContainer = new JPanel();
            groupContainer.setLayout(new BoxLayout(groupContainer, BoxLayout.Y_AXIS));
            groupContainer.setBackground(Settings.Styles.applicationBgColor);
            for(Integer dishId : dishGroup.getDishes()) {
                Dish dish = dishGroups.getDishes().getDish(dishId);
                SingleDishPanel dp = new SingleDishPanel(dish, dishGroup.getGroup(), remote);
                dishPanel.add(dp);
                groupContainer.add(dp);
            }
            addDishBtn = new JButton("Lägg till rätt");
            addDishBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (ae.getSource() instanceof JButton) {
                        Dishes dishes = dishGroups.getDishes();
                        try {
                            dishes.loadData();
                        } catch (DataSource.WrongKeyException ex) {
                            JOptionPane.showMessageDialog(MenuPanel.this,
                                    Settings.Strings.serverConnectionError,
                                    "Server error",
                                    JOptionPane.ERROR_MESSAGE);
                            if (remote != null) {
                                remote.setConnectionView();
                            }
                            return;
                        }
                        if (dishes.getRows() <= 0) {

                            JOptionPane.showMessageDialog(MenuPanel.this,
                                    Settings.Strings.noDishesCreatedError,
                                    "Inga tillgängliga rätter",
                                    JOptionPane.WARNING_MESSAGE);
                            if (remote != null) {
                                remote.setViewDishes();
                            }
                            return;
                        }
                        JButton actionButton = (JButton) ae.getSource();
                        JPanel groupContainer = (JPanel) actionButton.getParent();

                        SingleDishPanel dp = new SingleDishPanel(dishes.getDishByIndex(0), dishGroup.getGroup(), remote);
                        dishPanel.add(dp);
                        groupContainer.remove(actionButton);
                        groupContainer.add(dp);
                        groupContainer.add(actionButton);
                        MenuPanel.this.revalidate();
                    }
                }
            });
            addDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, addDishBtn.getPreferredSize().height));
            groupContainer.add(addDishBtn);
            add(groupContainer);
        }
        
        submitBtn = new JButton(Settings.Strings.submit);
        submitBtn.addActionListener(syncGroupEvent);
        add(submitBtn);
        add(Box.createGlue());
    }

    public void setViewSwitch(Controller c) {
        this.remote = c;
    }

    class SingleDishPanel extends JPanel {

        String groupName = null;
        Dish dish;
        JComboBox<Dish> myComboBox;
        public SingleDishPanel(Dish dish, String gn, final Controller c) {
            this.dish = dish;
            this.groupName = gn;

            /**
             * Set layout
             */
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            setBackground(Settings.Styles.applicationBgColor);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            /**
             * Create removebutton
             */
            JButton remove = new JButton("X");
            add(remove, BorderLayout.WEST);
            /**
             * Create combobox - Inefficient to iterate through list every time
             */
            myComboBox = new JComboBox<>();
            populateComboBox(dish);

            myComboBox.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                    try {
                        MenuPanel.this.dishGroups.getDishes().loadData();
                    } catch (DataSource.WrongKeyException ex) {
                        JOptionPane.showMessageDialog(MenuPanel.this,
                            Settings.Strings.serverConnectionError,
                            "Server error",
                            JOptionPane.ERROR_MESSAGE);
                        MenuPanel.this.remote.setConnectionView();
                    }
                    myComboBox.removeAllItems();
                    populateComboBox(SingleDishPanel.this.dish);
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent pme) {
                }
            });
            myComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            add(myComboBox, BorderLayout.CENTER);

            remove.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    int n = ConfirmationBox.confirm(SingleDishPanel.this, "Ta bort " + myComboBox.getSelectedItem().toString() + "?");
                    if(n == 0){
                        
                        dishPanel.remove(SingleDishPanel.this);
                        Container parent = SingleDishPanel.this.getParent();
                        parent.remove(SingleDishPanel.this);
                        parent.revalidate();
                    }
                }
            });
        }
        
        final void populateComboBox(Dish dish) {
            for (Dish d : dishGroups.getDishes()) {
                myComboBox.addItem(d);
                if(d.equals(dish))
                    myComboBox.setSelectedItem(d);
            }
        }
        public final String getGroupName() {
            return groupName;
        }
    }
    private String printDishesPDF(){
        try {
            String url = "getpdfdishes";
            URL obj = new URL(Settings.Strings.serverURL + url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "User-Agent");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return "Too bad";
            }
            
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            if(service != null) {
                PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();
                InputStreamReader fin = new InputStreamReader(con.getInputStream());
                DocPrintJob job = service.createPrintJob();
                job.addPrintJobListener(new PrintJobListener() {

                    @Override
                    public void printDataTransferCompleted(PrintJobEvent pje) {
                    }

                    @Override
                    public void printJobCompleted(PrintJobEvent pje) {
                    }

                    @Override
                    public void printJobFailed(PrintJobEvent pje) {
                        printError();
                    }

                    @Override
                    public void printJobCanceled(PrintJobEvent pje) {
                    }

                    @Override
                    public void printJobNoMoreEvents(PrintJobEvent pje) {
                    }

                    @Override
                    public void printJobRequiresAttention(PrintJobEvent pje) {
                    }
                });
                Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
                job.print(doc, pras);
            }
            else {
                printError();
            }

        } catch (IOException | PrintException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    void printError() {
        JOptionPane.showMessageDialog(MenuPanel.this,
            "Det gick inte skriva ut dina configurerade rätter.\n "
                    + "Har du konfigurerat din standardskrivare ännu?",
            "Server error",
            JOptionPane.ERROR_MESSAGE);
    }
}
