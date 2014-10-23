package se.miun.dt142g.inventory;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Ingredient;
import se.miun.dt142g.food.Inventory;
import se.miun.dt142g.inventory.IngredientPanel.IngredientFieldListener;

/**
 * Inventory administration panel. Keeps track of ingredients and allows user to
 * add ingredients and syncronize ingredients with server.
 *
 * @author ulf
 * @see JPanel
 * @see IngredientPanel
 */
public class InventoryPanel extends JPanel {

    private final JButton addIngredient;
    private Inventory inventory;
    private IngredientFieldListener ingredientFieldListener = null; 
    final Controller remote; 

    /**
     * Constructor sets up layout and adds event listeners to buttons. Also
     * loads ingredients from database into local list of ingredients.
     *
     * @throws se.miun.dt142g.DataSource.WrongKeyException when there fails to
     * connect to server
     */
    public InventoryPanel(Controller c) throws DataSource.WrongKeyException {
        this.remote = c; 
        
        this.inventory = new Inventory();
        this.addIngredient = new JButton("Lägg till ingrediens");

        this.inventory.dbConnect();
        this.inventory.loadData();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Settings.Styles.applicationBgColor);
        this.add(addIngredient);
        for (Ingredient ingredient : inventory) {
            addIngredientPanel(ingredient);
        }


        addIngredient.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        this.addIngredient.addActionListener(new ActionListener() {

            /**
             * Event listener for addIngredient button, adds a IngredientPanel
             * to layout and adds the ingredient for that IngredientPanel to
             * local list of ingredients
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Ingredient ing = new Ingredient(inventory.getUniqueId(), "", 0);
                inventory.addIngredient(ing);
                update();
            }
        });
    }

    private void addIngredientPanel(Ingredient ingredient) {
        IngredientPanel ip = new IngredientPanel(ingredient, remote);
        ip.setIngredientFieldListener(ingredientFieldLIstener);
        this.add(ip);
    }

    private void update() {
        inventory.update();
        removeAll();
        add(addIngredient);
        for (Ingredient ing : inventory) {
            addIngredientPanel(ing);
        }
        remote.setSavedTab((JComponent) InventoryPanel.this, true);
        revalidate();
        repaint();
    }

    private final IngredientFieldListener ingredientFieldLIstener = new IngredientFieldListener() {
        @Override
        public void onFieldEdit() {
            update();
        }
    };
}