//Teclado Helena is a keyboard designed to better the experience mainly of users with cerebral palsy.
//        Copyright (C) 2024  Instituto de Pesquisas Tecnológicas
//This file is part of Teclado Helena.
//
//        Teclado Helena is free software: you can redistribute it and/or modify
//        it under the terms of the GNU General Public License as published by
//        the Free Software Foundation, either version 3 of the License, or
//        (at your option) any later version.
//
//        Teclado Helena is distributed in the hope that it will be useful,
//        but WITHOUT ANY WARRANTY; without even the implied warranty of
//        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//        GNU General Public License for more details.
//
//        You should have received a copy of the GNU General Public License
//        along with Teclado Helena. If not, see <https://www.gnu.org/licenses/>6.

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
