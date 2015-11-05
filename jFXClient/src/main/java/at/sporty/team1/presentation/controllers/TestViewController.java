package at.sporty.team1.presentation.controllers;

import at.sporty.team1.presentation.controllers.core.JfxController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class TestViewController extends JfxController {
    @FXML private TreeView<String> _treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CheckBoxTreeItem<String> rootElement = new CheckBoxTreeItem<>("ROOT");
        rootElement.setExpanded(true);

        _treeView.setRoot(rootElement);
        _treeView.setCellFactory(CheckBoxTreeCell.forTreeView());

        for (int i = 0; i < 10; ++i) {
            CheckBoxTreeItem<String> item = new CheckBoxTreeItem<>(Integer.toString(i));

            for (int j = 0; j < 4; ++j) {
                CheckBoxTreeItem<String> subItem = new CheckBoxTreeItem<>(Integer.toString(j));
                item.getChildren().add(subItem);
            }
            rootElement.getChildren().add(item);
        }
    }
}
