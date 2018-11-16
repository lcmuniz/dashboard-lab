package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.ui.*;
import io.vavr.control.Option;

import java.util.Optional;

public class ResourceView extends VerticalLayout {

    public ResourceView(InterSCityService interSCityService, IndexUI indexUI, AbstractResourcesView backView, String uuid) {

        Resource resource = interSCityService.findResource(uuid);
        Label titleLabel = new Label("Resource");
        titleLabel.addStyleName("h2");

        TextField idTextField = new TextField("ID");
        TextField uuidTextField = new TextField("UUID");
        TextField descriptionTextField = new TextField("Description");
        TextField capabilitiesTextField = new TextField("Capabilities");
        TextField createAtTextField = new TextField("Created at");
        TextField updatedAtTextField = new TextField("Updated at");
        TextField latitudeTextField = new TextField("Latitude");
        TextField longitudeTextField = new TextField("Longitude");
        TextField cityTextField = new TextField("City");
        TextField neighborhoodTextField = new TextField("Neighborhood");
        TextField stateTextField = new TextField("State");
        TextField postalCodeTextField = new TextField("Postal Code");
        TextField statusTextField = new TextField("Status");

        idTextField.setReadOnly(true);
        uuidTextField.setReadOnly(true);
        descriptionTextField.setReadOnly(true);
        capabilitiesTextField.setReadOnly(true);
        createAtTextField.setReadOnly(true);
        updatedAtTextField.setReadOnly(true);
        latitudeTextField.setReadOnly(true);
        longitudeTextField.setReadOnly(true);
        cityTextField.setReadOnly(true);
        neighborhoodTextField.setReadOnly(true);
        stateTextField.setReadOnly(true);
        postalCodeTextField.setReadOnly(true);
        statusTextField.setReadOnly(true);

        idTextField.setValue(String.valueOf(resource.getId()));
        uuidTextField.setValue(resource.getUuid());
        descriptionTextField.setValue(resource.getDescription());
        capabilitiesTextField.setValue(resource.getCapabilities().toString());
        createAtTextField.setValue(String.valueOf(resource.getCreatedAt()));
        updatedAtTextField.setValue(String.valueOf(resource.getUpdatedAt()));
        latitudeTextField.setValue(String.valueOf(resource.getLat()));
        longitudeTextField.setValue(String.valueOf(resource.getLon()));
        cityTextField.setValue(Option.of(resource.getCity()).getOrElse(""));
        neighborhoodTextField.setValue(Option.of(resource.getNeighborhood()).getOrElse(""));
        stateTextField.setValue(Option.of(resource.getState()).getOrElse(""));
        postalCodeTextField.setValue(Option.of(resource.getPostalCode()).getOrElse(""));
        statusTextField.setValue(resource.getStatus());

        Button backButton = new Button("Back");
        backButton.addClickListener(e -> {
            indexUI.getContentPanel().setContent(backView);
        });

        addComponents(
                titleLabel,
                new HorizontalLayout(idTextField, uuidTextField),
                descriptionTextField,
                capabilitiesTextField,
                new HorizontalLayout(latitudeTextField, longitudeTextField),
                new HorizontalLayout(cityTextField, neighborhoodTextField, stateTextField, postalCodeTextField),
                new HorizontalLayout(statusTextField, createAtTextField, updatedAtTextField),
                backButton);

    }

}
