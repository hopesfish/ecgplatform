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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
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
	 */
	public static void createChart(Float[] data, int start, int length, String path) {
		final XYDataset dataset = createDataset(data, start, length);
		final JFreeChart chart = createChart(dataset);

		writeFile(chart, path);
	}
	
	public static byte[] createChart(Float[] data, int start, int length) throws IOException {
		final XYDataset dataset = createDataset(data, start, length);
		final JFreeChart chart = createChart(dataset);	
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			BufferedImage chartImage = chart.createBufferedImage(1600, 300, 1,
					null);
			ImageIO.write(chartImage, "JPEG" , out);
			return out.toByteArray();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static XYDataset createDataset(Float[] data, int start, int length) {
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
				"", // chart title
				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, false);
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;

	}

	private static void writeFile(JFreeChart chart, String path) {
		try {
			OutputStream out;
			out = new FileOutputStream(path);

			BufferedImage chartImage = chart.createBufferedImage(1600, 300, 1,
					null);
			ImageIO.write(chartImage, "JPEG", out);

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}