import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Application extends JFrame implements ActionListener{
    JButton open=null;
    public static void main(String[] args) {
        new Application();
    }
    public Application(){
        open=new JButton("open");
        this.add(open);
        this.setBounds(400, 200, 100, 100);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        open.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        jfc.showDialog(new JLabel(), "Select wav file...");
        File file=jfc.getSelectedFile();
        if(file.isDirectory()){
            System.out.println("Folder:"+file.getAbsolutePath());
        }else if(file.isFile()){
            System.out.println("File:"+file.getAbsolutePath());
            Main.drawWaveFile(file.getAbsolutePath());
        }
        System.out.println(jfc.getSelectedFile().getName());

    }



}
