package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.ApplicationMenuBar;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lombok.Getter;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;

@SpringUI(path = "/")
@Theme("valo")
@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class IndexUI extends UI {

    @Getter
    private Panel contentPanel;

    private InterSCityService interSCityService;

    public IndexUI(InterSCityService interSCityService) {
        this.interSCityService = interSCityService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        contentPanel = new Panel();
        contentPanel.setSizeFull();

        contentPanel.setContent(new IndexView(interSCityService, this));

        ApplicationMenuBar applicationMenuBar = new ApplicationMenuBar(interSCityService, contentPanel,  this);
        applicationMenuBar.setHeightUndefined();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth("100%");
        verticalLayout.setHeight("100%");
        verticalLayout.addComponent(applicationMenuBar);
        verticalLayout.addComponent(contentPanel);
        verticalLayout.setExpandRatio(contentPanel, 1);

        setContent(verticalLayout);
        setHeight("100%");

    }

}
