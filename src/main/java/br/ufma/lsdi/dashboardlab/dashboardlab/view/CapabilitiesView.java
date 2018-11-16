package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capabilities;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

import java.util.Optional;

public class CapabilitiesView extends  VerticalLayout {

    protected InterSCityService interSCityService;

    protected Grid<Capability> capabilityGrid;


    public CapabilitiesView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        Label titleLabel = new Label("Capabilities");
        titleLabel.addStyleName("h2");

        TextField searchTextField = new TextField();
        searchTextField.setWidth("100%");
        searchTextField.setPlaceholder("Search for name or description");
        searchTextField.setSizeFull();
        searchTextField.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                search(searchTextField.getValue());
            }
        });

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> search(searchTextField.getValue()));

        capabilityGrid = new Grid<>();
        capabilityGrid.setSizeFull();
        capabilityGrid.addColumn(Capability::getId).setCaption("ID");
        capabilityGrid.addColumn(Capability::getName).setCaption("Name");
        capabilityGrid.addColumn(Capability::getDescription).setCaption("Description");
        capabilityGrid.addColumn(Capability::getFunction).setCaption("Function");

        capabilityGrid.addItemClickListener(e -> {
            if(e.getMouseEventDetails().isDoubleClick()) {
                Optional<Capability> item = capabilityGrid.getSelectionModel().getFirstSelectedItem();
                if (item.isPresent()) {
                    String name = item.get().getName();
                    indexUI.getContentPanel().setContent(new CapabilityView(interSCityService, indexUI, this, name));
                }
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponentsAndExpand(searchTextField);
        horizontalLayout.addComponent(searchButton);

        addComponents(titleLabel, horizontalLayout, capabilityGrid);

        search("");
    }

    protected void search(String value) {
        if (value.equals("")) {
            capabilityGrid.setItems(interSCityService.findAllCapabilities());
        }
        else {
            capabilityGrid.setItems(
                    interSCityService.findAllCapabilities()
                            .stream()
                            .filter(capability -> {
                                String name = capability.getName() == null ? "" : capability.getName().toLowerCase();
                                String description = capability.getDescription() == null ? "" : capability.getDescription().toLowerCase();
                                return name.contains(value.toLowerCase()) || description.contains(value.toLowerCase());
                            })
            );
        }

    }

}
