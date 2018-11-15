package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class IndexView extends VerticalLayout {

    public IndexView(InterSCityService interSCityService) {

        Label titleLabel = new Label("Dashboard");

        titleLabel.addStyleName("h2");
        
        addComponent(titleLabel);

    }

}
