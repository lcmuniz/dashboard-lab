package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.ui.*;
import io.vavr.control.Option;

public class CapabilityView extends VerticalLayout {

    public CapabilityView(InterSCityService interSCityService, IndexUI indexUI, AbstractCapabilitiesView backView, String name) {

        Capability capability = interSCityService.findCapability(name);
        Label titleLabel = new Label("Capability");
        titleLabel.addStyleName("h2");

        TextField idTextField = new TextField("ID");
        TextField nameTextField = new TextField("Name");
        TextField descriptionTextField = new TextField("Description");
        TextField functionTextField = new TextField("Function");

        idTextField.setReadOnly(true);
        nameTextField.setReadOnly(true);
        descriptionTextField.setReadOnly(true);
        functionTextField.setReadOnly(true);

        idTextField.setValue(String.valueOf(capability.getId()));
        nameTextField.setValue(capability.getName());
        descriptionTextField.setValue(Option.of(capability.getDescription()).getOrElse(""));
        functionTextField.setValue(capability.getFunction());

        Button backButton = new Button("Back");
        backButton.addClickListener(e -> {
            indexUI.getContentPanel().setContent(backView);
        });

        addComponents(
                titleLabel,
                idTextField,
                nameTextField,
                descriptionTextField,
                functionTextField,
                backButton);

    }

}
