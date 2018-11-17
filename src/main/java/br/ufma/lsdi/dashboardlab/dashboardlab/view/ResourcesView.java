package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.ui.*;
import org.vaadin.ui.NumberField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResourcesView extends VerticalLayout {

    private InterSCityService interSCityService;

    private Grid<Resource> resourceGrid;

    private TextField capabilityTextField;
    private TextField statusTextField;
    private NumberField latitudeTextField;
    private NumberField longitudeTextField;
    private NumberField radiusTextField;

    public ResourcesView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        Label titleLabel = new Label("Resource");
        titleLabel.addStyleName("h2");

        capabilityTextField = new TextField("Capability");
        statusTextField = new TextField("Status");
        latitudeTextField = new NumberField ("Latitude");
        longitudeTextField = new NumberField ("Longitude");
        radiusTextField = new NumberField ("Radius");

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> search());

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        hl1.addComponents(capabilityTextField, statusTextField, latitudeTextField, longitudeTextField, radiusTextField, searchButton);
        Panel panel = new Panel();
        panel.setContent(hl1);

        resourceGrid = new Grid<>();
        resourceGrid.setSizeFull();
        resourceGrid.addColumn(Resource::getId).setCaption("ID");
        resourceGrid.addColumn(Resource::getUuid).setCaption("UUID");
        resourceGrid.addColumn(Resource::getUri).setCaption("URI");
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
        resourceGrid.addColumn(Resource::getCollectInterval).setCaption("Collect Interval");

        resourceGrid.addItemClickListener(e -> {
            if(e.getMouseEventDetails().isDoubleClick()) {
                Optional<Resource> item = resourceGrid.getSelectionModel().getFirstSelectedItem();
                if (item.isPresent()) {
                    String uuid = item.get().getUuid();
                    indexUI.getContentPanel().setContent(new ResourceView(interSCityService, indexUI, this, uuid));
                }
            }
        });

        addComponents(titleLabel, hl1, resourceGrid);

        search();
    }

    private void search() {

        Map<String, String> params = new HashMap<>();

        if (!capabilityTextField.getValue().isEmpty()) {
            params.put("capability", capabilityTextField.getValue());
        }
        if (!statusTextField.getValue().isEmpty()) {
            params.put("status", statusTextField.getValue());
        }
        if (!latitudeTextField.getValue().isEmpty()) {
            params.put("latitude", latitudeTextField.getValue());
        }
        if (!longitudeTextField.getValue().isEmpty()) {
            params.put("longitude", longitudeTextField.getValue());
        }
        if (!radiusTextField.getValue().isEmpty()) {
            params.put("radius", radiusTextField.getValue());
        }

        if (params.size() == 0) {
            resourceGrid.setItems(interSCityService.findAllResources());
        }
        else {
            resourceGrid.setItems(interSCityService.findAllResourcesByParams(params));
        }

    }

}
