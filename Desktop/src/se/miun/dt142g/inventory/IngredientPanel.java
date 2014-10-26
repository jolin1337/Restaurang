package se.miun.dt142g.inventory;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Ingredient;
import se.miun.dt142g.food.Dishes;

/**
 * JPanel for a single Ingredient. Fields update when their focus is lost.
 * Remove button "X" removes the panel and ingredient after confirmation from
 * user.
 *
 * @author ulf
 */
public class IngredientPanel extends JPanel {

    /**
     * Is the remove button for this ingredient
     */
    private final JButton close;
    /**
     * This field contains the name of this ingredient that the user can edit
     */
    private final JTextField ingredientName;
    /**
     * Informs us about the amount of this ingredient there are, user can edit
     */
    private final JTextField amount;
    /**
     * The datasource of this ingredient to take and set information to.
     */
    private final Ingredient ingredient;
    /**
     * Ingredient field listener to load data from IngredientPanel fields
     */
    private IngredientFieldListener ingredientFieldListener = null;
    /**
     * An instance to the controller class
     */
    final Controller remote;
    /**
     * Dishes data handling object
     */
    static Dishes dishes = new Dishes();

    /**
     * Constructor initializes the panel and adds focus listeners and click
     * listeners
     *
     * @param ingredient The Ingredient to represent in the panel
     */
    public IngredientPanel(final Ingredient ingredient, final Controller c) {
        this.ingredient = ingredient;
        this.remote = c;

        this.ingredientName = new JTextField(ingredient.getName());
        this.amount = new JTextField(Integer.toString(ingredient.getAmount()));
        this.close = new JButton("X");

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Settings.Styles.applicationBgColor);
        this.ingredientName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        ingredientName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Namn")));
        this.close.setMaximumSize(new Dimension(50, 80));
        amount.setColumns(3);
        amount.setMaximumSize(new Dimension(40, 80));
        amount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Port.")));

        amount.addFocusListener(ingredientFocusListener);
        amount.addKeyListener(ingredientKeyListener);
        ingredientName.addFocusListener(ingredientFocusListener);
        ingredientName.addKeyListener(ingredientKeyListener);

        close.addActionListener(new ActionListener() {

            /**
             * Event listener for close button with confirmation
             *
             * @param ae
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    dishes.loadData();
                } catch (DataSource.WrongKeyException ex) {
                    Logger.getLogger(IngredientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                List<String> dishNames = dishes.getDishNames(ingredient.getId());
                if (!dishNames.isEmpty()) {
                    String toDialog = "Kan inte ta bort ingrediensen,\nden används i:\n";
                    for (String s : dishNames) {
                        toDialog += s + "\n";
                    }
                    JOptionPane.showMessageDialog(IngredientPanel.this, toDialog);
                } else {
                    int n = ConfirmationBox.confirm(IngredientPanel.this, "Ta bort " + ingredientName.getText() + "?");
                    if (n == 0) {
                        ingredient.setFlaggedForRemoval(true);
                        updateFromFields();
                    }
                }
            }
        });

        this.add(Box.createRigidArea(new Dimension(1, 20)));
        this.add(close);
        this.add(ingredientName);
        this.add(amount);
        this.add(Box.createRigidArea(new Dimension(1, 20)));

    }

    /**
     * Returns the ingredient associated with this panel
     *
     * @return The ingredient associated with this panel
     */
    public Ingredient getIngredient() {
        return this.ingredient;
    }
    /**
     * focusListener to update data from fields
     */
    FocusListener ingredientFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            // do nothing
        }

        /**
         * Updates ingredients with data from fields when focus is lost
         *
         * @param
         */
        @Override
        public void focusLost(FocusEvent e) {
            updateFromFields();
        }
    };

    /**
     * Key listener object to set saved tab to false to display asterisk on tab
     * if the fields have been modified but not yet saved
     */
    KeyListener ingredientKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
            // do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                updateFromFields();
            } else {
                remote.setSavedTab((JComponent) IngredientPanel.this.getParent(), false);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //do nothing
        }
    };

    /**
     * Updates the fields and sync/uploads to the server
     */
    public void updateFromFields() {
        if (isInteger(this.amount.getText())) {
            this.ingredient.setAmount(Integer.parseInt(this.amount.getText()));
        } else {
            this.amount.setText(Integer.toString(this.ingredient.getAmount()));
        }
        this.ingredient.setName(this.ingredientName.getText());
        if (ingredientFieldListener != null) {
            ingredientFieldListener.onFieldEdit();
        }
    }

    /**
     * checks to see if Integer.parseInt(String s) is possible
     *
     * @param s String to check
     * @return true if the parameter s is numeric
     */
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * @return the userPanelListener
     */
    public IngredientFieldListener getIngredientFieldListener() {
        return ingredientFieldListener;
    }

    /**
     * @param ingredientFieldListener the IngredientFieldListener to set
     */
    public void setIngredientFieldListener(IngredientFieldListener ingredientFieldListener) {
        this.ingredientFieldListener = ingredientFieldListener;
    }

    /**
     * Interface to be implemented in InventoryPanel so to be able to update
     * data from the input fields of each ingredient panel
     */
    public interface IngredientFieldListener {

        public void onFieldEdit();
    }
}
