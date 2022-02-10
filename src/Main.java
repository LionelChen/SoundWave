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
            int[][] raw_data = reader.getData();
            int[] data = reader.getData()[0];

            JFrame frame = new JFrame( filename + "; Sample Rate: " +reader.getSampleRate() + "; BitsPerSample: " + reader.getBitPerSample() + "Waveform");
            frame.setLayout( new FlowLayout() );

            frame.getContentPane().add(processingRawData(raw_data[0], "Channel 0"));

            if(reader.getNumChannels()==2){
                frame.getContentPane().add(processingRawData(raw_data[1], "Channel 1"));
            }

            frame.pack();
            frame.setVisible(true);



        } else {
            System.err.println(filename + " might not be a valid wav file");
        }
    }


    public static ChartPanel processingRawData(int[] data, String channelNumber){
        final XYSeriesCollection dataset0 = new XYSeriesCollection();
        final XYSeries channel_0 = new XYSeries( channelNumber );
        for(int i = 0; i<data.length; i++){
            channel_0.add(i, data[i]);
        }
        dataset0.addSeries( channel_0 );

        JFreeChart xyChart1 =
                ChartFactory.createXYLineChart(channelNumber,"", "", dataset0,
                        PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel1 = new ChartPanel( xyChart1 );
        chartPanel1.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        return chartPanel1;
    }

    public static void main(String[] args) {
        drawWaveFile("piano2.wav");
        //drawWaveFile("rawwavs_wav_20_8_1_pcm.wav");
        //drawWaveFile("test1.wav");
        //drawWaveFile("organfinale.wav");
    }
}
