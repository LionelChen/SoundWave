import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

// Detailed wav format standard: http://soundfile.sapp.org/doc/WaveFormat/

@SuppressWarnings("unused")
public class WaveFileReader {

    // start of Wav file constants
    static public int LEN_RIFF_FILE_DESCRIPTION_HEADER = 4;
    static public int LEN_SIZE_OF_FILE = 4;
    static public int LEN_WAV_DESCRIPTION_HEADER = 4;
    static public int LEN_FMT_DESCRIPTION_HEADER = 4;
    static public int LEN_WAV_SECTION_CHUNK = 4;
    static public int LEN_WAV_TYPE_FORMAT = 2;
    static public int LEN_MONO_STEREO_FLAG = 2;
    static public int LEN_SAMPLE_FREQUENCY = 4;
    static public int LEN_BYTES_PER_SEC = 4;
    static public int LEN_BLOCK_ALIGNMENT = 2;
    static public int LEN_BITS_PER_SAMPLE = 2;
    static public int LEN_DATA_DESCRIPTION_HEADER = 4;
    static public int LEN_SIZE_OF_DATA_CHUNK = 4;

    public static String RIFF_FILE_DESCRIPTION_HEADER = "RIFF";
    public static String WAV_DESCRIPTION_FLAG = "WAVE";
    public static String FMT_DESCRIPTION_HEADER = "fmt ";
    public static String DATA_DESCRIPTION_HEADER = "data";
    // End of Wav file constants

    private String filename = null;
    private int[][] data = null;

    private int len = 0;

    //Contains the letters "RIFF" in ASCII form
    private String chunkdescriptor;
    //Size of chunk 1
    private long chunksize;
    //Contains the letters "WAVE"
    private String waveflag;
    private String fmtsubchunk;
    private long subchunk1size;
    private int audioformat;
    private int numchannels = 0;
    private long samplerate = 0;
    private long byterate = 0;
    private int blockalign = 0;
    private int bitspersample = 0;
    private String datasubchunk = null;
    private long subchunk2size = 0;
    private FileInputStream fis = null;
    private BufferedInputStream bis = null;

    private boolean issuccess = false;

    public WaveFileReader(String filename) {

        this.initReader(filename);
    }

    public static int[] readSingleChannel(String filename) {
        if (filename == null || filename.length() == 0) {
            return null;
        }
        try {
            WaveFileReader reader = new WaveFileReader(filename);
            int[] res = reader.getData()[0];
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // check if successfully read wav file
    public boolean isSuccess() {
        return issuccess;
    }

    // git bit per sample, it's either 8 bits or 18 bits
    public int getBitPerSample() {
        return this.bitspersample;
    }

    // get sample rate, should be an even number (between 0 and 35565?)
    public long getSampleRate() {
        return this.samplerate;
    }

    // get channel numbers, 1 for mono, 2 for stereo
    // 获取声道个数，1代表单声道 2代表立体声
    public int getNumChannels() {
        return this.numchannels;
    }

    // get data length, the total sample points number
    public int getDataLen() {
        return this.len;
    }

    // get frequency data, can be used directly to plot the waveform
    public int[][] getData() {
        return this.data;
    }

    private void initReader(String filename) {
        this.filename = filename;

        try {
            fis = new FileInputStream(this.filename);
            bis = new BufferedInputStream(fis);

            this.chunkdescriptor = readString(LEN_RIFF_FILE_DESCRIPTION_HEADER);
            if (!chunkdescriptor.endsWith("RIFF"))
                throw new IllegalArgumentException("RIFF miss, " + filename + " is not a wave file.");

            this.chunksize = readLong();
            this.waveflag = readString(LEN_WAV_DESCRIPTION_HEADER);
            if (!waveflag.endsWith("WAVE"))
                throw new IllegalArgumentException("WAVE miss, " + filename + " is not a wave file.");

            this.fmtsubchunk = readString(LEN_FMT_DESCRIPTION_HEADER);
            if (!fmtsubchunk.endsWith("fmt "))
                throw new IllegalArgumentException("fmt miss, " + filename + " is not a wave file.");

            this.subchunk1size = readLong();
            this.audioformat = readInt();
            this.numchannels = readInt();
            this.samplerate = readLong();
            this.byterate = readLong();
            this.blockalign = readInt();
            this.bitspersample = readInt();

            this.datasubchunk = readString(LEN_DATA_DESCRIPTION_HEADER);
            if (!datasubchunk.endsWith("data"))
                throw new IllegalArgumentException("data miss, " + filename + " is not a wave file.");
            this.subchunk2size = readLong();

            this.len = (int) (this.subchunk2size / (this.bitspersample / 8) / this.numchannels);

            this.data = new int[this.numchannels][this.len];

            // 读取数据
            for (int i = 0; i < this.len; ++i) {
                for (int n = 0; n < this.numchannels; ++n) {
                    if (this.bitspersample == 8) {
                        this.data[n][i] = bis.read();
                    } else if (this.bitspersample == 16) {
                        this.data[n][i] = this.readInt();
                    }
                }
            }

            issuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (fis != null)
                    fis.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private String readString(int len) {
        byte[] buf = new byte[len];
        try {
            if (bis.read(buf) != len)
                throw new IOException("no more data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buf);
    }

    private int readInt() {
        byte[] buf = new byte[2];
        int res = 0;
        try {
            if (bis.read(buf) != 2)
                throw new IOException("no more data");
            res = (buf[0] & 0x000000FF) | (((int) buf[1]) << 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private long readLong() {
        long res = 0;
        try {
            long[] l = new long[4];
            for (int i = 0; i < 4; ++i) {
                l[i] = bis.read();
                if (l[i] == -1) {
                    throw new IOException("no more data");
                }
            }
            res = l[0] | (l[1] << 8) | (l[2] << 16) | (l[3] << 24);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private byte[] readBytes(int len) {
        byte[] buf = new byte[len];
        try {
            if (bis.read(buf) != len)
                throw new IOException("no more data");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }
}