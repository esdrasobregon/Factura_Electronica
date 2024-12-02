/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import entitys.TipoCambio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import logic.AppStaticValues;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author eobregon
 */
public class CommonFreeLineChart {

    ArrayList<TipoCambio> listaTipoCambio;
    int option;
    CategoryPlot lineCategoryPlot;
    LineAndShapeRenderer lineRenderer;
    double min = -1;
    double max = 0;
    double mid = 0;

    public CommonFreeLineChart(ArrayList<TipoCambio> listaTipoCambio, int option) {
        this.listaTipoCambio = listaTipoCambio;
        this.option = option;
    }

    public void setCategoriPlotUpPosition(int degree) {
        switch (degree) {
            case 45:
                lineCategoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
                break;
            case 90:
                lineCategoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                break;

        }
    }

    public void hideShowLbsAxisX(boolean show) {
        lineCategoryPlot.getDomainAxis().setVisible(show);
    }

    /**
     * this method
     *
     * @param option 0 for compras, 1 for ventas
     */
    private double getSum() {
        double sumSelectedValue = 0;
        if (option == 0) {
            for (TipoCambio tc : listaTipoCambio) {
                sumSelectedValue += tc.getCompra();
            }
        } else {
            for (TipoCambio tc : listaTipoCambio) {
                sumSelectedValue += tc.getVenta();
            }
        }
        return sumSelectedValue;
    }

    public void showCompraLineChart(JPanel pnl, JTextArea lbInfo, String categoryAxisLabel, String valueAxisLabel) {
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        mid = getSum() / listaTipoCambio.size();
        setMax();
        min = max;
        setMin();

        datos = getDataSet();

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Fluctuación del Colón en la media",
                categoryAxisLabel,
                valueAxisLabel,
                datos,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        lineCategoryPlot = lineChart.getCategoryPlot();
        lineCategoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        lineCategoryPlot.setBackgroundPaint(Color.BLACK);
        lineCategoryPlot.getRangeAxis().setUpperBound(max + 1);
        lineCategoryPlot.getRangeAxis().setLowerBound(min - 1);

        //lineCategoryPlot.getLegendItems().get(0).setLabelFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 8));
        if (listaTipoCambio.size() > 31) {
            //lineCategoryPlot.getRangeAxis().setVisible(false);
            lineCategoryPlot.getDomainAxis().setVisible(false);
        }
        NumberFormat currency = NumberFormat.getCurrencyInstance(AppStaticValues.localeCrCurrency);
        NumberAxis rangeAxis = (NumberAxis) lineCategoryPlot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(currency);

        //lineCategoryPlot.getDomainAxis().setLabelFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 8));
        lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(0, 255, 0);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //lineRenderer.setBaseItemLabelFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 8));
        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(400, 280));
        lineChartPanel.setMouseWheelEnabled(true);

        pnl.removeAll();
        pnl.setLayout(new BorderLayout());
        pnl.add(lineChartPanel, BorderLayout.NORTH);

        lbInfo.setText("Max: " + AppStaticValues.numberFormater.format(max)
                + "\nMin: " + AppStaticValues.numberFormater.format(min)
                + "\nMedia: " + AppStaticValues.numberFormater.format(mid));
    }

    private DefaultCategoryDataset getDataSet() {
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        int count = 0;
        boolean flag = false;
        if (option == 0) {
            while (count < listaTipoCambio.size() && !flag) {
                TipoCambio tca = listaTipoCambio.get(count);

                datos.setValue((tca.getCompra()),
                        "Fecha", AppStaticValues.shortDateformatter.format(tca.getFecha()).toString());
                count++;
            }
        } else {
            while (count < listaTipoCambio.size() && !flag) {
                TipoCambio tca = listaTipoCambio.get(count);

                datos.setValue((tca.getVenta()),
                        "Fecha", AppStaticValues.shortDateformatter.format(tca.getFecha()).toString());
                count++;
            }
        }
        return datos;
    }

    public void showMediaLineAxisX(boolean selected) {
        if (selected) {
           ValueMarker xMarker = new ValueMarker(mid);
            xMarker.setPaint(Color.red);
            this.lineCategoryPlot.addRangeMarker(xMarker);
        } else {
            
            this.lineCategoryPlot.clearRangeMarkers();
        }
    }

    public void showPoints(boolean show) {
        lineRenderer.setShapesVisible(show);
    }

    public void showGridLines(boolean show) {
        this.lineCategoryPlot.setRangeGridlinesVisible(show);
    }

    public void showLines(boolean show) {
        lineRenderer.setSeriesLinesVisible(0, show);
    }

    private void setMin() {
        if (option == 0) {
            for (TipoCambio t : listaTipoCambio) {
                if (this.min > t.getCompra()) {
                    min = t.getCompra();
                }
            }
        } else {
            for (TipoCambio t : listaTipoCambio) {
                if (this.min > t.getVenta()) {
                    min = t.getVenta();
                }
            }
        }
    }

    private void setMax() {
        if (option == 0) {
            for (TipoCambio t : listaTipoCambio) {
                if (this.max < t.getCompra()) {
                    max = t.getCompra();
                }
            }
        } else {
            for (TipoCambio t : listaTipoCambio) {
                if (this.max < t.getVenta()) {
                    max = t.getVenta();
                }
            }
        }
    }

}
