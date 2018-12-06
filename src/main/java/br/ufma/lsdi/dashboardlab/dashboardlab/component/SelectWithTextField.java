package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import com.vaadin.event.*;
import com.vaadin.event.selection.MultiSelectionEvent;
import com.vaadin.event.selection.MultiSelectionListener;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectWithTextField extends VerticalLayout {

    TextField text;
    ListSelect<String> list;
    List<String> items;

    ShortcutListener enterListener = new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
        @Override
        public void handleAction(Object sender, Object target) {
            TextField tf = (TextField) target;
            items.add(tf.getValue());
            tf.clear();
            list.setItems(items);
        }
    };

    ShortcutListener deleteListener = new ShortcutListener("", ShortcutAction.KeyCode.DELETE, null) {
        @Override
        public void handleAction(Object sender, Object target) {
            items.removeAll(list.getSelectedItems());
                list.setItems(items);
        }
    };

    public SelectWithTextField(String caption) {
        super();
        text = new TextField(caption);
        text.setSizeFull();

        text.addFocusListener(e -> addShorcut(text));
        text.addBlurListener(e -> removeShortcut(text));

        items = new ArrayList<>();
        list = new ListSelect<String>();
        list.setWidth("100%");


        list.addContextClickListener(new ContextClickEvent.ContextClickListener() {
            @Override
            public void contextClick(ContextClickEvent contextClickEvent) {
                System.out.println(contextClickEvent.getMouseEventDetails().isDoubleClick());
            }
        });

        setMargin(false);
        setSpacing(false);

        addComponent(text);
        addComponent(list);


    }

    private void removeShortcut(TextField text) {
            text.removeShortcutListener(enterListener);
    }

    private void addShorcut(TextField text) {
        text.addShortcutListener(enterListener);
    }

    public void removeListShortcut() {
        list.removeShortcutListener(deleteListener);
    }

    public void addListShorcut() {
        list.addShortcutListener(deleteListener);
    }

    public List<String> getItems() {
        return items;
    }


}
