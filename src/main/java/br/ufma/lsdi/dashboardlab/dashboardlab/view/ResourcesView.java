package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;

public class ResourcesView extends VerticalLayout {

    private InterSCityService interSCityService;

    private Grid<Resource> resourceGrid;

    public ResourcesView(InterSCityService interSCityService) {

        this.interSCityService = interSCityService;

        Label titleLabel = new Label("Resources");
        titleLabel.addStyleName("h2");

        TextField searchTextField = new TextField();
        searchTextField.setWidth("100%");
        searchTextField.setPlaceholder("Search for description");
        searchTextField.setSizeFull();
        searchTextField.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                search(searchTextField.getValue());
            }
        });

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> search(searchTextField.getValue()));

        resourceGrid = new Grid<>();
        resourceGrid.setSizeFull();
        resourceGrid.addColumn(Resource::getId).setCaption("ID");
        resourceGrid.addColumn(Resource::getUuid).setCaption("UUID");
        resourceGrid.addColumn(Resource::getUri).setCaption("URI");
        resourceGrid.addColumn(Resource::getDescription).setCaption("Description");
        resourceGrid.addColumn(Resource::getCreatedAt).setCaption("Created at");
        resourceGrid.addColumn(Resource::getUpdatedAt).setCaption("Updated at");
        resourceGrid.addColumn(Resource::getLat).setCaption("Latitude");
        resourceGrid.addColumn(Resource::getLon).setCaption("Longitude");
        resourceGrid.addColumn(Resource::getCity).setCaption("City");
        resourceGrid.addColumn(Resource::getNeighborhood).setCaption("Neighborhood");
        resourceGrid.addColumn(Resource::getState).setCaption("State");
        resourceGrid.addColumn(Resource::getPostalCode).setCaption("Postal Code");
        resourceGrid.addColumn(Resource::getStatus).setCaption("Status");
        resourceGrid.addColumn(Resource::getCollectInterval).setCaption("Collect Interval");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponentsAndExpand(searchTextField);
        horizontalLayout.addComponent(searchButton);

        addComponents(titleLabel, horizontalLayout, resourceGrid);

        search("");
    }

    private void search(String value) {
        if (value.equals("")) {
            resourceGrid.setItems(interSCityService.findAll());
        }
        else {
            resourceGrid.setItems(
                    interSCityService.findAll()
                            .stream()
                            .filter(resource -> resource.getDescription().toLowerCase().contains(value.toLowerCase()))
            );
        }

    }

}
