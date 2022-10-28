package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ResultsTimeSeries extends JFrame {

    private static final long serialVersionUID = 1L;
    JTextField testField = new JTextField(10);

    public ResultsTimeSeries() {


    }

    public ResultsTimeSeries(XYSeriesCollection series, String title) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, "Period", "Value",
                series, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cp = new ChartPanel(chart) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(720, 240);
            }
        };
        cp.setMouseWheelEnabled(true);
        add(cp);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        setSize(1000, 520);
        setVisible(true);
    }


}