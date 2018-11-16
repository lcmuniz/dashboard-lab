package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class IndexView extends VerticalLayout {

    public IndexView(InterSCityService interSCityService, IndexUI indexUI) {

        Label titleLabel = new Label("Dashboard");

        titleLabel.addStyleName("h2");

        addComponent(titleLabel);

    }

}
