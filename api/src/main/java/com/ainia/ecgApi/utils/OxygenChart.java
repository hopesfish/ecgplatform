package com.ainia.ecgApi.utils;

import java.awt.Color;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class OxygenChart {
	public static void createChart(File file, byte[] data, int start, int length, int chartWidth, int chartHeight) {
		final XYDataset dataset = createDataset(data, start, length);
		final JFreeChart chart = createChart(dataset);
		try {
			ChartUtilities.saveChartAsPNG(file, chart, chartWidth, chartHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static XYDataset createDataset(byte[] data, int start, int length) {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries series = new XYSeries("");
		for (int i = start; i < start + length; i++) {
			series.add(i, (double) data[i]);
		}
		xySeriesCollection.addSeries(series);
		return xySeriesCollection;
	}

	private static JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
			"", // chart
			"X", // x axis label
			"Oxygen", // y axis label
			dataset, // data
			PlotOrientation.VERTICAL, true, // include legend
			true, // tooltips
			false // urls
		);

		chart.setBackgroundPaint(Color.white);
		chart.setBorderVisible(false);
		chart.removeLegend();

		final XYPlot plot = chart.getXYPlot();

		plot.setBackgroundPaint(Color.white);
		/*
		 * plot.setDomainGridlinePaint(Color.red);
		 * plot.setDomainGridlinesVisible(false);
		 * plot.setDomainMinorGridlinesVisible(false);
		 * 
		 * plot.setRangeGridlinePaint(Color.red);
		 * plot.setRangeGridlinesVisible(true);
		 * plot.setRangeMinorGridlinesVisible(false);
		 */

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.BLACK);
		plot.setRenderer(renderer);

		final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setTickLabelsVisible(true);
		domainAxis.setVisible(false);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setTickLabelsVisible(false);
		rangeAxis.setVisible(true);

		/*
		 * double vMarkerInterval = 0.0; if (max <= 4.0f) {
		 * rangeAxis.setRange(-4.0, 4.0); rangeAxis.setTickUnit(new
		 * NumberTickUnit(0.2)); vMarkerInterval = 1.0; } else {
		 * rangeAxis.setRange(-8.0, 8.0); rangeAxis.setTickUnit(new
		 * NumberTickUnit(0.4)); vMarkerInterval = 2.0; }
		 * 
		 * int count = dataset.getItemCount(0); for (int i = 0; i < count;
		 * i+=50) { final Marker marker = new ValueMarker(i);
		 * marker.setPaint(Color.red); plot.addDomainMarker(marker); }
		 * 
		 * for (int i = -4; i <=4; i++) { final Marker marker = new
		 * ValueMarker(i*vMarkerInterval); marker.setPaint(Color.red);
		 * plot.addRangeMarker(marker); }
		 */

		return chart;

	}
}
