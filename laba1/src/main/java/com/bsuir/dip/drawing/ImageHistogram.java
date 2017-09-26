package com.bsuir.dip.drawing;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.util.List;

public final class ImageHistogram {
    private final String title;

    private static final String X_AXIS_TITLE = "Pixels";
    private static final String Y_AXIS_TITLE = "Frequency";

    public static final String X_AXIS_NAME = "xAxis";
    public static final String Y_AXIS_NAME = "yAxis";

    private static final String SERIES_TITLE = "Image histogram";

    public static final int INTERVAL_COUNT = 256;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final List<Integer> xAxis;
    private final List<Integer> yAxis;

    public ImageHistogram(final String title, List<Integer> xAxis, List<Integer> yAxis) {
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    /**
     * Show image's histogram.
     */
    public void show() {
        CategoryChart chart = getChart();
        new SwingWrapper<>(chart).displayChart();
    }

    private CategoryChart getChart() {
        CategoryChart chart = new CategoryChartBuilder()
                .width(WIDTH).height(HEIGHT)
                .title(title)
                .xAxisTitle(X_AXIS_TITLE)
                .yAxisTitle(Y_AXIS_TITLE)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);

        chart.addSeries(SERIES_TITLE, xAxis, yAxis);

        return chart;
    }
}
