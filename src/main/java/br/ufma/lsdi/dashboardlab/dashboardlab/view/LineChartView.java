package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.BasicLine;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.ui.*;

import java.util.List;

public class LineChartView extends VerticalLayout {

    private final InterSCityService interSCityService;

    Gson gson = AppGson.get();

    TextField uuidTextField;
    TextField capabilityTextField;
    TextField contextDataXTextField;
    TextField contextDataYTextField;


    public LineChartView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        VerticalLayout map = createPanel();

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        //grid.setColumnExpandRatio(0, 0.6f);
        //grid.setColumnExpandRatio(1, 0.4f);
        grid.addComponent(map);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createPanel() {

        VerticalLayout vl = new VerticalLayout();

        uuidTextField = new TextField("UUID");
        capabilityTextField = new TextField("Capability");
        contextDataXTextField = new TextField("Context Data (X)");
        contextDataYTextField = new TextField("Context Data (Y)");
        Button button = new Button("Plot Line Char");
        button.addClickListener(e -> plotLineChart(vl));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        horizontalLayout.addComponents(uuidTextField, capabilityTextField, contextDataXTextField, contextDataYTextField, button);

        vl.addComponent(horizontalLayout);

        return vl;
    }

    private void plotLineChart(VerticalLayout vl) {

        String[] str = (contextDataXTextField.getValue() + "," + contextDataYTextField.getValue()).split(",");
        List<List<Object>> values = interSCityService.findAllDataValue(uuidTextField.getValue(), capabilityTextField.getValue(), str);

        vl.addComponent(new BasicLine(
                capabilityTextField.getValue(),
                contextDataXTextField.getValue(),
                contextDataYTextField.getValue(),
                values).getChart());

    }

}
