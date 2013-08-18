/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * -------------------
 * LineChartDemo6.java
 * -------------------
 * (C) Copyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: LineChartDemo6.java,v 1.5 2004/04/26 19:11:55 taqua Exp $
 *
 * Changes
 * -------
 * 27-Jan-2004 : Version 1 (DG);
 * 
 */
package com.ainia.ecgApi.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * This class is used to create ECG chart. Call createChart function to create
 * ECG chart.
 * 
 * @author yuccai
 * 
 */
public class ECGChart {
	
	public static byte[] createChart(String label, float[] data, float max , int start, int length, float tickUnit) throws IOException {
		return createChart(label, data, max , start, length  , 1600 , 100);
	}
	/**
	 * 
	 * @param data
	 *            chart data
	 * @param start
	 *            start index
	 * @param length
	 *            data length
	 * @param path
	 *            output path
	 * @throws IOException
	 */
	public static byte[] createChart(String label, float[] data, float max, int start, int length, int chartWidth, int chartHeight) throws IOException {
		final XYDataset dataset = createDataset(data, start, length);
		final JFreeChart chart = createChart(label, dataset, max);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			BufferedImage chartImage = 
				chart.createBufferedImage(chartWidth, chartHeight, BufferedImage.TYPE_INT_RGB, null);
			ImageIO.write(chartImage, "JPEG", out);
			return out.toByteArray();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}
	
	private static XYDataset createDataset(float[] data, int start, int length) {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries series = new XYSeries("");
		for (int i = start; i < start + length; i++) {
			series.add(i, (double) data[i]);
		}
		xySeriesCollection.addSeries(series);
		return xySeriesCollection;
	}
	
	private static JFreeChart createChart(String label, final XYDataset dataset, float max) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
			"", // chart title
			"X", // x axis label
			label, // y axis label
			dataset, // data
			PlotOrientation.VERTICAL,
			true, // include legend
			true, // tooltips
			false // urls
		);

		chart.setBackgroundPaint(Color.white);
		chart.setBorderVisible(false);
		chart.removeLegend();

		final XYPlot plot = chart.getXYPlot();
		
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.red);
		plot.setDomainGridlinesVisible(false);
		plot.setDomainMinorGridlinesVisible(false);
		
		plot.setRangeGridlinePaint(Color.red);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeMinorGridlinesVisible(false);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		renderer.setSeriesPaint(0, Color.black);
		plot.setRenderer(renderer);

		final NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setTickLabelsVisible(false);
		domainAxis.setVisible(false);
		
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setAutoRange(false);
		rangeAxis.setTickLabelsVisible(false);
		rangeAxis.setVisible(true);
		
		double vMarkerInterval = 0.0;
		if (max <= 4.0f) {
			rangeAxis.setRange(-4.0, 4.0);
			rangeAxis.setTickUnit(new NumberTickUnit(0.2));
			vMarkerInterval = 1.0;
		} else {
			rangeAxis.setRange(-8.0, 8.0);
			rangeAxis.setTickUnit(new NumberTickUnit(0.4));
			vMarkerInterval = 2.0;
		}
		
		int count = dataset.getItemCount(0);
		for (int i = 0; i < count; i+=50) {
			final Marker marker = new ValueMarker(i);
			marker.setPaint(Color.red);
			plot.addDomainMarker(marker);
		}
		
		for (int i = -4; i <=4; i++) {
			final Marker marker = new ValueMarker(i*vMarkerInterval);
			marker.setPaint(Color.red);
			plot.addRangeMarker(marker);
		}

 		return chart;

	}

}
