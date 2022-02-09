import java.awt.*;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class XYLineChart_AWT extends ApplicationFrame {

    public XYLineChart_AWT( String applicationTitle, String chartTitle, int[] data ) {
        super(applicationTitle);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        final XYSeries channel_0 = new XYSeries( "Channel_0" );
        for(int i = 0; i<data.length; i++){
            channel_0.add(i, data[i]);
        }
        dataset.addSeries( channel_0 );

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "" ,
                "" ,
                dataset ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        JFreeChart xylineChart1 = ChartFactory.createXYLineChart(
                chartTitle ,
                "" ,
                "" ,
                dataset ,
                PlotOrientation.VERTICAL ,
                true , true , false);


        ChartPanel chartPanel = new ChartPanel( xylineChart );
        ChartPanel chartPanel1 = new ChartPanel(xylineChart1);
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        //renderer.setSeriesPaint( 1 , Color.GREEN );
        //renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        double size = 1.0;
        double delta = size / 2.0;
        Shape shape1 = new Rectangle2D.Double(-delta, -delta, size, size);
        renderer.setSeriesShape(0, shape1, false);
        //renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        //renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer( renderer );
        getContentPane().add(chartPanel);
        //setContentPane( chartPanel );
        getContentPane().add(chartPanel1);
        pack();
    }

    private XYDataset createDataset( ) {
        final XYSeries firefox = new XYSeries( "Firefox" );
        firefox.add( 1.0 , 1.0 );
        firefox.add( 2.0 , 4.0 );
        firefox.add( 3.0 , 3.0 );

        final XYSeries chrome = new XYSeries( "Chrome" );
        chrome.add( 1.0 , 4.0 );
        chrome.add( 2.0 , 5.0 );
        chrome.add( 3.0 , 6.0 );

        final XYSeries iexplorer = new XYSeries( "InternetExplorer" );
        iexplorer.add( 3.0 , 4.0 );
        iexplorer.add( 4.0 , 5.0 );
        iexplorer.add( 5.0 , 4.0 );

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( firefox );
        dataset.addSeries( chrome );
        dataset.addSeries( iexplorer );
        return dataset;
    }

}