package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class IndexView extends VerticalLayout {

    public IndexView(InterSCityService interSCityService) {
        addComponent(new Button("Ok"));
    }

}
