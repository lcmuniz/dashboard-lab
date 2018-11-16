package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

import java.util.Optional;

public abstract class AbstractResourcesView extends VerticalLayout {

    protected InterSCityService interSCityService;

    protected Grid<Resource> resourceGrid;

    public AbstractResourcesView(InterSCityService interSCityService, String title, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        Label titleLabel = new Label(title);
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
        resourceGrid.addColumn(Resource::getDescription).setCaption("Description");
        resourceGrid.addColumn(Resource::getCapabilities).setCaption("Capabilities");
        resourceGrid.addColumn(Resource::getCreatedAt).setCaption("Created at");
        resourceGrid.addColumn(Resource::getUpdatedAt).setCaption("Updated at");
        resourceGrid.addColumn(Resource::getLat).setCaption("Latitude");
        resourceGrid.addColumn(Resource::getLon).setCaption("Longitude");
        resourceGrid.addColumn(Resource::getCity).setCaption("City");
        resourceGrid.addColumn(Resource::getNeighborhood).setCaption("Neighborhood");
        resourceGrid.addColumn(Resource::getState).setCaption("State");
        resourceGrid.addColumn(Resource::getPostalCode).setCaption("Postal Code");
        resourceGrid.addColumn(Resource::getStatus).setCaption("Status");

        resourceGrid.addItemClickListener(e -> {
            if(e.getMouseEventDetails().isDoubleClick()) {
                Optional<Resource> item = resourceGrid.getSelectionModel().getFirstSelectedItem();
                if (item.isPresent()) {
                    String uuid = item.get().getUuid();
                    indexUI.getContentPanel().setContent(new ResourceView(interSCityService, indexUI, this, uuid));
                }
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponentsAndExpand(searchTextField);
        horizontalLayout.addComponent(searchButton);

        addComponents(titleLabel, horizontalLayout, resourceGrid);

        search("");
    }

    protected abstract void search(String value);

}
