
package br.ipt.thl.fx;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class FxStackPane extends StackPane {

    public FxStackPane() {
        super();
        setFocusTraversable(false);
        setFocused(false);
    }

    public void add(final Node node,
                    final Pos pos) {
        add(node, pos, new Insets(0));
    }

    public void add(final Node node,
                    final Pos pos,
                    final Insets margin) {
        add(node);
        setAlignment(node, pos);
        setMargin(node, margin);
    }

    public void add(Node node) {
        childrens().add(node);
    }


    public void addOnCenter(final Node node) {
        add(node, Pos.CENTER);
    }

    public void addOnBottomCenter(final Node node) {
        add(node, Pos.BOTTOM_CENTER);
    }

    private ObservableList<Node> childrens() {
        return getChildren();
    }

    public void addStyleClass(final String styleClass) {
        if (!containsStyleClass(styleClass)) {
            getStyleClass().add(styleClass);
        }
    }

    public boolean containsStyleClass(final String styleClass) {
        return getStyleClass().contains(styleClass);
    }
    
}
