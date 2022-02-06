import javax.swing.*;
import com.sin.java.plot.Plot;
import com.sin.java.plot.PlotFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;

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
            XYLineChart_AWT chart = new XYLineChart_AWT(
                    filename + "; Sample Rate: " +reader.getSampleRate() + "; BitsPerSample: " + reader.getBitPerSample() + "; # of Channel: " +reader.getNumChannels(),
                    "Waveform", data);
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );



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
