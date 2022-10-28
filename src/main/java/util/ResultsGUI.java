package util;

import model.Model;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsGUI extends JFrame implements ActionListener {

    JButton btnAveragePrice = new JButton("Average price");
    JButton btnNTransactions = new JButton("Number of transactions");
    JButton btnNForSale = new JButton("Number of flats for sale");
    JButton btnNNewLoanContracts = new JButton("Number of new loan contracts");
    JButton btnNewLoanVolume = new JButton("Volume of new loans");

    public ResultsGUI() {
        this.setBounds(100, 200, 1060, 450);
        setLayout(null);

        add(btnAveragePrice);
        btnAveragePrice.setBounds(10, 20, 130, 20);
        add(btnNTransactions);
        btnNTransactions.setBounds(160, 20, 130, 20);
        add(btnNForSale);
        btnNForSale.setBounds(310, 20, 130, 20);
        add(btnNNewLoanContracts);
        btnNNewLoanContracts.setBounds(460, 20, 130, 20);
        add(btnNewLoanVolume);
        btnNewLoanVolume.setBounds(610, 20, 130, 20);


        btnAveragePrice.addActionListener(this);
        btnNTransactions.addActionListener(this);
        btnNForSale.addActionListener(this);
        btnNNewLoanContracts.addActionListener(this);
        btnNewLoanVolume.addActionListener(this);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == btnAveragePrice) {
            showSeries(Model.averagePrice, "Average price");
        } else if (e.getSource() == btnNTransactions) {
            showSeries(Model.nTransactions, "Number of transactions");
        } else if (e.getSource() == btnNForSale) {
            showSeries(Model.nForSale, "Number of flats for sale");
        } else if (e.getSource() == btnNNewLoanContracts) {
            showSeries(Model.nNewLoanContracts, "Number of new loan contracts");
        } else if (e.getSource() == btnNewLoanVolume) {
            showSeries(Model.newLoanVolume, "Volume of new loans");
        }


    }

    public void showSeries(double[] values, String legend) {
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries newSeries = new XYSeries(legend);
        for (int i = 0; i < values.length; i++) {
            newSeries.add(i, values[i]);
        }
        series.addSeries(newSeries);

        ResultsTimeSeries resultsTimeSeries = new ResultsTimeSeries(series, legend);
    }

}
