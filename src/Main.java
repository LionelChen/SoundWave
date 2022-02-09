import javax.swing.*;
import com.sin.java.plot.Plot;
import com.sin.java.plot.PlotFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;

import java.awt.*;

public class Main {
    // int 数组 转换到 double数组
    // JavaPlot 只支持double数组的绘制
    public static double[] Integers2Doubles(int[] raw) {
        double[] res = new double[raw.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = raw[i];
        }
        return res;
    }

    // 绘制波形文件
    public static void drawWaveFile(String filename) {
        WaveFileReader reader = new WaveFileReader(filename);
        //
        String[] pamss = new String[] { "-r", "-g", "-b" };
        if (reader.isSuccess()) {

/*            PlotFrame frame = Plot.figrue(String.format("%s %dHZ %dBit %dCH", filename, reader.getSampleRate(), reader.getBitPerSample(), reader.getNumChannels()));
            frame.setSize(500, 200);
            Plot.hold_on();

            for (int i = 0; i < reader.getNumChannels(); ++i) {
                // 获取i声道数据
                int[] data = reader.getData()[i];
                // 绘图
                Plot.plot(Integers2Doubles(data), pamss[i % pamss.length]);
            }
            Plot.hold_off();*/

            int[][] raw_data = reader.getData();
            int[] data = reader.getData()[0];
/*            XYLineChart_AWT chart1 = new XYLineChart_AWT(
                    filename + "; Sample Rate: " +reader.getSampleRate() + "; BitsPerSample: " + reader.getBitPerSample() + "; # of Channel: " +reader.getNumChannels(),
                    "Waveform", raw_data[0]);

            chart1.pack();
            RefineryUtilities.centerFrameOnScreen( chart1 );
            chart1.setVisible( true );*/

            JFrame frame = new JFrame( filename + "; Sample Rate: " +reader.getSampleRate() + "; BitsPerSample: " + reader.getBitPerSample() + "Waveform");
            frame.setLayout( new FlowLayout() );

            final XYSeriesCollection dataset0 = new XYSeriesCollection();
            final XYSeries channel_0 = new XYSeries( "Channel_0" );
            for(int i = 0; i<raw_data[0].length; i++){
                channel_0.add(i, raw_data[0][i]);
            }
            dataset0.addSeries( channel_0 );

            JFreeChart xyChart1 =
                    ChartFactory.createXYLineChart("Channel_0","", "", dataset0,
                            PlotOrientation.VERTICAL, true, true, false);

            ChartPanel chartPanel1 = new ChartPanel( xyChart1 );
            chartPanel1.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

            frame.getContentPane().add(chartPanel1);


            final XYSeriesCollection dataset1 = new XYSeriesCollection();
            final XYSeries channel_1 = new XYSeries( "Channel_0" );
            for(int i = 0; i<raw_data[1].length; i++){
                channel_1.add(i, raw_data[1][i]);
            }
            dataset1.addSeries( channel_1 );

            JFreeChart xyChart2 =
                    ChartFactory.createXYLineChart("Channel_1","", "", dataset1,
                            PlotOrientation.VERTICAL, true, true, false);

            ChartPanel chartPanel2 = new ChartPanel( xyChart2 );
            chartPanel2.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

            frame.getContentPane().add(chartPanel2);


            frame.pack();

            frame.setVisible(true);



        } else {
            System.err.println(filename + " might not be a valid wav file");
        }
    }

    public static void main(String[] args) {
        drawWaveFile("piano2.wav");
        //drawWaveFile("rawwavs_wav_20_8_1_pcm.wav");
        //drawWaveFile("test1.wav");
        //drawWaveFile("organfinale.wav");
    }
}
