/*
 * This code is created for one purpose only. And should not be used for any 
 * other purposes unless the author of this file has apporved. 
 * 
 * This code is a piece of a project in the course DT142G on Mid. Sweden university
 * Created by students for this projekt only
 */
package se.miun.dt142g.food;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import se.miun.dt142g.ConfirmationBox;
import se.miun.dt142g.Controller;
import se.miun.dt142g.DataSource;
import se.miun.dt142g.Settings;
import se.miun.dt142g.data.Dish;
import se.miun.dt142g.data.Ingredient;

/**
 * This view class displays the detail of an dish
 *
 * @author Johannes Lindén
 * @since 2014-10-10
 * @version 1.2
 */
public class DishDetailPanel extends JPanel {

    /**
     * The dish to edit
     */
    Dish dish = null;
    /**
     * The available ingredients the dish can have
     */
    Inventory inventory = new Inventory();
    /**
     * Add a new Ingredient button
     */
    JButton addIngBtn = new JButton("Lägg till ingrediens");
    /**
     * A save and return button to DishPanel view
     */
    JButton saveDishBtn;
    /**
     * A JPanel container for all ingredients in this DishDetailPanel
     */
    JPanel ingredientsContainer;
    /**
     * The remote controller to switch tab-view
     */
    Controller remote = null;
    /**
     * The list of ingredient combo boxes this DishDetailPanel have
     */
    List<JComboBox> ingredientsComboBoxes;
    /**
     * JPanel container for one ingredient
     */
    List<JPanel> ingredientPanelList;
    /**
     * The event triggered when the remove ("X") button are invoked
     */
    ActionListener removeEvent = null;
    /**
     * A indication that the price is wrong formated
     */
    JLabel inputErrorLabel = new JLabel("Vänligen ange ett pris.");
    /**
     * The price JTextField
     */
    JTextField price;
    /**
     * The name JTextField
     */
    JTextField name;
    /**
     * A boolean indicating if this dish is saved
     */
    boolean saved = true;

    /**
     * Constructs a dish detail panel.
     *
     * @param dish - The dish this view will control. dish can be null, in this
     * case there are no components initiated in the view (it will be
     * empty/blank)
     * @param c - The controller for view changes
     * @throws se.miun.dt142g.DataSource.WrongKeyException If there was an
     * connection error
     */
    public DishDetailPanel(Dish dish, final Controller c) throws DataSource.WrongKeyException {
        remote = c;

        setBackground(Settings.Styles.applicationBgColor);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (dish == null) {
            return;
        }
        setDish(dish);

    }

    /**
     * Create a new JLabel for general purposes
     *
     * @param labelName - The text this label will have
     * @return A new JLabel
     */
    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box fixHeight = Box.createHorizontalBox();
        fixHeight.add(label);
        fixHeight.add(Box.createHorizontalGlue());
        add(fixHeight);
        return label;
    }

    /**
     * Create a new JTextField for general purposes
     *
     * @param labelName - The text this field will have
     * @return A new JTextField
     */
    private JTextField addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        textField.setMinimumSize(new Dimension(0, 60));
        add(textField);
        return textField;
    }

    /**
     * The remove event listener setter
     *
     * @param removeE - The listener this panel will use
     */
    public void setRemoveEventListener(ActionListener removeE) {
        removeEvent = removeE;
    }

    /**
     * Creates a complete new DishDetailPanel content with new components
     * describing the new dish d
     *
     * @param d - A different dish this panel will control
     * @throws se.miun.dt142g.DataSource.WrongKeyException When there was any
     * connection errors.
     */
    public final void setDish(Dish d) throws DataSource.WrongKeyException {
        inventory.dbConnect();
        inventory.loadData();
        removeAll();
        addIngBtn = new JButton("Lägg till ingrediens");

        dish = d;
        if (removeEvent != null) {
            JButton removeBtn = new JButton("X");
            removeBtn.addActionListener(removeEvent);
            add(removeBtn);
        }

        addLabel("Namn");
        name = addTextField(dish.getName());
        name.addKeyListener(textFieldKeyListener);
        ingredientsContainer = new JPanel();
        ingredientsContainer.setLayout(new BoxLayout(ingredientsContainer, BoxLayout.Y_AXIS));
        addLabel("Ingredienser");
        List<Integer> ingredients = dish.getIngredients();
        ingredientsComboBoxes = new ArrayList<>();
        ingredientPanelList = new ArrayList<>();
        if (ingredients != null) {
            for (Integer ingredient : ingredients) {
                JButton remove = new JButton("X");
                remove.setMaximumSize(new Dimension(35, 35));
                remove.addActionListener(removeIngredientListener);
                JComboBox<Ingredient> jListInventory = new JComboBox<>();

                for (Ingredient ing : inventory) {
                    jListInventory.addItem(ing);
                    if (ing.getId() == ingredient) {
                        jListInventory.setSelectedItem(ing);
                    }
                }
                jListInventory.addActionListener(itemChangeListener);
                //JTextField ingEdit = new JTextField(name);
                JPanel horiView = new JPanel();
                horiView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                horiView.setLayout(new BoxLayout(horiView, BoxLayout.LINE_AXIS));
                horiView.add(remove);
                horiView.add(jListInventory);
                ingredientPanelList.add(horiView);
                ingredientsComboBoxes.add(jListInventory);
                ingredientsContainer.add(horiView);
            }
        }
        ingredientsContainer.setMaximumSize(ingredientsContainer.getPreferredSize());
        add(ingredientsContainer);
        addIngBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(addIngBtn);
        addIngBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (inventory.getIngredients().isEmpty()) {
                    String message = "Det finns inga ingredienser i inventariet,\nvill du lägga till ingredienser nu?";
                    int n = ConfirmationBox.confirm(DishDetailPanel.this, message);
                    if (n == 0) {
                        remote.setViewInventory();
                    }
                    return;
                }
                JButton remove = new JButton("X");
                remove.setMaximumSize(new Dimension(35, 35));
                remove.addActionListener(removeIngredientListener);

                JComboBox<Ingredient> jListInventory = new JComboBox<>();
                for (Ingredient ing : inventory) {
                    jListInventory.addItem(ing);
                }
                JPanel horiView = new JPanel();
                horiView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                horiView.setLayout(new BoxLayout(horiView, BoxLayout.LINE_AXIS));
                horiView.add(remove);
                horiView.add(jListInventory);
                ingredientPanelList.add(horiView);
                ingredientsComboBoxes.add(jListInventory);
                ingredientsContainer.add(horiView);
                ingredientsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, ingredientsContainer.getPreferredSize().height));
                ingredientsContainer.revalidate();
                saved = false;
                remote.setSavedTab(DishDetailPanel.this, saved);
            }
        });
        addLabel("Pris (kr)");
        NumberFormat moneyFormat = new DecimalFormat("#0.00");

        price = addTextField(moneyFormat.format(dish.getPrice()));
        price.addKeyListener(textFieldKeyListener);

        saveDishBtn = new JButton("Spara rätt");
        saveDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(saveDishBtn);

        add(inputErrorLabel);
        inputErrorLabel.setVisible(false);
        saveDishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dish.setName(name.getText());
                inputErrorLabel.setVisible(false);
                //reformat the price string 
                try {
                    String formattedPrice = price.getText();
                    formattedPrice = formattedPrice.replaceAll(",", ".");//change , to .
                    formattedPrice = formattedPrice.replaceAll("[^0-9.]", ""); // remove all nonnumerics, except for .

                    dish.setPrice(Float.parseFloat(formattedPrice));
                } catch (NumberFormatException e) {
                    inputErrorLabel.setVisible(true);
                    System.out.println("NumberFormatException");
                    return;
                }
                dish.getIngredients().clear();

                for (JComboBox ingredientsComboBox : ingredientsComboBoxes) {
                    Object ingredient = ingredientsComboBox.getSelectedItem();
                    if (ingredient instanceof Ingredient) {
                        dish.addIngredient(((Ingredient) ingredient).getId());
                    }
                }

                remote.setViewDishes(saved);
            }
        });
    }
    /**
     * The method invoked remove button ("X") is pressed
     */
    ActionListener removeIngredientListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource().getClass() != JButton.class) {
                return;
            }
            JButton btn = (JButton) ae.getSource();
            if (btn.getParent() == null) {
                return;
            }
            int ingredientIndex = ingredientPanelList.indexOf(btn.getParent());
            ingredientPanelList.remove(ingredientIndex);
            ingredientsComboBoxes.remove(ingredientIndex);
            Container parent = btn.getParent().getParent();
            parent.remove(btn.getParent());
            saved = false;
            remote.setSavedTab(DishDetailPanel.this, saved);
            parent.revalidate();
            parent.repaint();
        }
    };
    /**
     * The method invoked when a key is pressed on a JTextField then we update
     * the tab saved indicator
     */
    KeyListener textFieldKeyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent ke) {
            saved = false;
            remote.setSavedTab(DishDetailPanel.this, saved);
        }

        @Override
        public void keyPressed(KeyEvent ke) {
        }

        @Override
        public void keyReleased(KeyEvent ke) {
        }
    };

    /**
     * This method is invoked when a ingredient has changed. Then we change the
     * ingredient in datasource file as well
     */
    ActionListener itemChangeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            remote.setSavedTab(DishDetailPanel.this, false);
        }
    };
}
