package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.ApplicationMenuBar;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.ContextDataQueryRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.ResourceQueryRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;

@SpringUI(path = "/")
@Theme("valo")
@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class IndexUI extends UI {

    @Getter
    private Panel contentPanel;

    private InterSCityService interSCityService;
    private ResourceQueryRepository resourceQueryRepository;
    private ContextDataQueryRepository contextDataQueryRepository;

    public IndexUI(InterSCityService interSCityService,
                   ResourceQueryRepository resourceQueryRepository,
                   ContextDataQueryRepository contextDataQueryRepository) {
        this.interSCityService = interSCityService;
        this.resourceQueryRepository = resourceQueryRepository;
        this.contextDataQueryRepository = contextDataQueryRepository;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        contentPanel = new Panel();
        contentPanel.setSizeFull();

        contentPanel.setContent(new IndexView(interSCityService, this));

        ApplicationMenuBar applicationMenuBar = new ApplicationMenuBar(interSCityService, resourceQueryRepository, contextDataQueryRepository, contentPanel,  this);
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
