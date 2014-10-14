/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.food;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import miun.dt142g.Controller;
import miun.dt142g.DataSource;
import miun.dt142g.data.Dish;
import miun.dt142g.data.Ingredient;

/**
 *
 * @author Johannes
 */
public class DishDetailPanel extends JPanel {

    Dish dish = null;
    Inventory inv = new Inventory();
    JButton addIngBtn = new JButton("Lägg till ingrediens");
    JButton saveDishBtn;
    JPanel ingredientsContainer;
    Controller remote = null;
    List<JComboBox> ingredientsComboBoxes;
    List<JPanel> ingredientPanelList;

    public DishDetailPanel(Dish dish, final Controller c) throws DataSource.WrongKeyException {
        remote = c;

        setBackground(Color.white);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (dish == null) {
            return;
        }
        setDish(dish);

    }

    private JLabel addLabel(String labelName) {
        JLabel label = new JLabel("<html><div style='margin: 10px 0 3px 3px;'>" + labelName + "</div></html>");
        Box fixHeight = Box.createHorizontalBox();
        fixHeight.add(label);
        fixHeight.add(Box.createHorizontalGlue());
        add(fixHeight);
        return label;
    }

    private JTextField addTextField(String textName) {
        JTextField textField = new JTextField(textName);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        add(textField);
        return textField;
    }

    public final void setDish(Dish d) throws DataSource.WrongKeyException {
        inv.dbConnect();
        inv.loadData();
        removeAll();
        addIngBtn = new JButton("Lägg till ingrediens");

        dish = d;
        addLabel("Namn");
        final JTextField name = addTextField(dish.getName());
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
                JComboBox jListInventory = new JComboBox();
                for (Ingredient ing : inv) {
                    jListInventory.addItem(ing);
                    if (ing.getId() == ingredient) {
                        jListInventory.setSelectedItem(ing);
                    }
                }
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
                JButton remove = new JButton("X");
                remove.setMaximumSize(new Dimension(35, 35));
                remove.addActionListener(removeIngredientListener);

                JComboBox jListInventory = new JComboBox();
                String name = "";
                for (Ingredient ing : inv) {
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
            }
        });
        addLabel("Pris");
        final JTextField price = addTextField(Float.toString(dish.getPrice()));
        saveDishBtn = new JButton("Spara rätt");
        saveDishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        add(saveDishBtn);
        saveDishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dish.setName(name.getText());
                dish.setPrice(Float.parseFloat(price.getText()));

                dish.getIngredients().clear();

                for (JComboBox ingredientsComboBox : ingredientsComboBoxes) {
                    Object ingredient = ingredientsComboBox.getSelectedItem();
                    if (ingredient instanceof Ingredient) {
                        dish.addIngredient(((Ingredient) ingredient).getId());
                    }
                }

                remote.setViewDishes();
            }
        });
    }
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
            parent.revalidate();
            parent.repaint();
        }
    };
}
