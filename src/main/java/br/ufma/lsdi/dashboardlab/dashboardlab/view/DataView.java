package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.TextListComponent;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capabilities;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.PostDataCollector;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataView extends VerticalLayout {

    private final InterSCityService interSCityService;

    private TwinColSelect<String> resourcesSelect;
    private TwinColSelect<String> capabilitiesSelect;
    private TextListComponent matchersList;
    private DateField startDate;
    private DateField endDate;

    Grid<Resource> resourceGrid;

    public DataView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        Label titleLabel = new Label("All Data");
        titleLabel.addStyleName("h2");

        VerticalLayout tab1 = new VerticalLayout();
        tab1.setCaption("Search Form");
        tab1.addComponent(tab1());

        VerticalLayout tab2 = new VerticalLayout();
        tab2.setCaption("Search Results");
        tab2.addComponent(tab2());

        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(tab1);
        tabSheet.addTab(tab2);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponents(titleLabel, tabSheet);
        addComponent(verticalLayout);

    }

    private VerticalLayout tab1() {

        resourcesSelect = getResourcesSelect();
        capabilitiesSelect = getCapabilitiesSelect();
        matchersList = getMatchersList();

        startDate = new DateField("Start Date");
        endDate = new DateField("End Date");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.addComponents(startDate, endDate);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        horizontalLayout.addComponent(resourcesSelect);
        horizontalLayout.addComponent(capabilitiesSelect);
        horizontalLayout.addComponent(matchersList);
        horizontalLayout.addComponent(verticalLayout);

        Button searchButton = new Button("Search");
        searchButton.addClickListener(e -> search());

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setMargin(false);
        verticalLayout1.setSpacing(false);
        verticalLayout1.addComponents(searchButton, horizontalLayout);
        verticalLayout1.setComponentAlignment(searchButton, Alignment.MIDDLE_RIGHT);
        return verticalLayout1;
    }

    private void search() {
        PostDataCollector pdc = new PostDataCollector();
        if (resourcesSelect.getSelectedItems().size() > 0) {
            pdc.setUuids(new ArrayList<>(resourcesSelect.getSelectedItems()));
        }
        if (capabilitiesSelect.getSelectedItems().size() > 0) {
            pdc.setCapabilities(new ArrayList<>(capabilitiesSelect.getSelectedItems()));
        }
        if (matchersList.getSize() > 0) {
            pdc.setMatchers(matchersList.getMatchers());
        }
        if (!startDate.isEmpty()) {
            pdc.setStartRange(startDate.getValue());
        }
        if (!endDate.isEmpty()) {
            pdc.setStartRange(endDate.getValue());
        }

        List<Resource> resources = interSCityService.findAllData(pdc);
        resourceGrid.setItems(resources);

    }

    private TextListComponent getMatchersList() {
        return new TextListComponent();
    }

    private TwinColSelect getResourcesSelect() {
        TwinColSelect<String> select =
                new TwinColSelect<>("Select Resources");

        List<Resource> resources = interSCityService.findAllResources();

        List<String> items = resources.stream().map(resource -> resource.getUuid()).collect(Collectors.toList());
        select.setItems(items);

        select.setRows(items.size());

        //        select.addSelectionListener(event ->
        //                layout.addComponent(
        //                        new Label("Selected: " + event.getNewSelection())));

        return select;
    }

    private TwinColSelect getCapabilitiesSelect() {
        TwinColSelect<String> select =
                new TwinColSelect<>("Select Capabilities");

        List<Capability> capabilities = interSCityService.findAllCapabilities();

        List<String> items = capabilities.stream()
                .map(capability -> capability.getName()).collect(Collectors.toList());
        select.setItems(items);

        select.setRows(items.size());

        //        select.addSelectionListener(event ->
        //                layout.addComponent(
        //                        new Label("Selected: " + event.getNewSelection())));

        return select;
    }

    private VerticalLayout tab2() {

        resourceGrid = new Grid<>();
        resourceGrid.setSizeFull();
        resourceGrid.addColumn(Resource::getId).setCaption("ID");
        resourceGrid.addColumn(Resource::getUuid).setCaption("UUID");
        resourceGrid.addColumn(Resource::getUri).setCaption("URI");
        resourceGrid.addColumn(Resource::getDescription).setCaption("Description");
        resourceGrid.addColumn(Resource::getCapabilitiesAsList).setCaption("Capabilities");
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

        return new VerticalLayout(resourceGrid);
    }

}
