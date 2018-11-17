package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class TextListComponent extends VerticalLayout {

    List<String> list = new ArrayList<>();

    public TextListComponent() {

        setMargin(false);
        setSpacing(false);

        ListSelect<String> listSelect = new ListSelect<>();
        listSelect.setSizeFull();

        TextField textField = new TextField("Add Matchers");
        textField.setWidth("100%");
        textField.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (!textField.getValue().isEmpty()) list.add(textField.getValue());
                textField.setValue("");
                listSelect.setItems(list);
            }
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setSizeFull();
        verticalLayout.addComponent(listSelect);
        verticalLayout.addLayoutClickListener(e -> {
            if (e.getMouseEventDetails().isDoubleClick()) {
                if (listSelect.getSelectedItems().size() > 0) {
                    String element = (String) listSelect.getSelectedItems().toArray()[0];
                    list.remove(element);
                    listSelect.setItems(list);
                }
            };

        });
        addComponents(textField, verticalLayout);

    }

    public int getSize() {
        return list.size();
    }

    public String getMatchers() {
        return "{" + String.join(",", list) + "}";
    }

}
