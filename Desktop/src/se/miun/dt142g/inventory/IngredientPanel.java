package se.miun.dt142g.inventory;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Ingredient;
import se.miun.dt142g.food.Inventory;

/**
 * JPanel for a single Ingredient. Fields update when their focus is lost.
 * Remove button "X" removes the panel and ingredient after confirmation from
 * user.
 *
 * @author ulf
 */
public class IngredientPanel extends JPanel {

    /**
     * Is the remove button fot this ingredient
     */
    private final JButton close;
    /**
     * This field containst the name of this ingredient that the user can edit
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
     * Uses a reference to the inventory to sync this ingredient to the server
     */
    private final Inventory inventory;

    /**
     * Constructor initializes the panel and adds focus listeners and click
     * listeners
     *
     * @param ingredient The Ingredient to represent in the panel
     */
    public IngredientPanel(final Ingredient ingredient, final Inventory inventory) {
        this.inventory = inventory;
        this.ingredient = ingredient;

        this.ingredientName = new JTextField(ingredient.getName());
        this.amount = new JTextField(Integer.toString(ingredient.getAmount()));
        this.close = new JButton("X");

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Settings.Styles.applicationBgColor);
        this.ingredientName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        ingredientName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Namn")));
        this.close.setMaximumSize(new Dimension(50, 70));
        amount.setColumns(3);
        amount.setMaximumSize(new Dimension(40, 70));
        amount.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Settings.Styles.applicationBgColor), BorderFactory.createTitledBorder("Port.")));

        amount.addFocusListener(ingredientFocusListener);
        ingredientName.addFocusListener(ingredientFocusListener);

        close.addActionListener(new ActionListener() {

            /**
             * Event listener for close button with confirmation
             *
             * @param ae
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                int n = ConfirmationBox.confirm(IngredientPanel.this, ingredientName.getText());
                if (n == 0) {
                    Container parent = IngredientPanel.this.getParent();
                    parent.remove(IngredientPanel.this);
                    inventory.update();
                    parent.revalidate();
                    parent.repaint();
                    ingredient.setFlaggedForRemoval(true);
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

    FocusListener ingredientFocusListener = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            //do nothing
        }

        /**
         * Updates ingredients with data from fields when focus is lost
         *
         * @param e
         */
        @Override
        public void focusLost(FocusEvent e) {
            updateFromFields();
        }
    };

    /**
     * Updates the fields and sync/uploads to the server
     */
    public void updateFromFields() {
        if (isInteger(this.amount.getText())) {
            this.ingredient.setAmount(Integer.parseInt(this.amount.getText()));
        }
        this.ingredient.setName(this.ingredientName.getText());
        inventory.update();
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
}
