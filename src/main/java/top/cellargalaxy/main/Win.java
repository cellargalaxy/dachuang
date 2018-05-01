package top.cellargalaxy.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by cellargalaxy on 18-4-30.
 */
public class Win extends JFrame {
    private final JFileChooser confJsonJFileChooser;
    private final JButton confJsonJButton;
    private final JTextField countJTextField;
    private final JButton startJButton;
    private File confJsonFile;

    public static void main(String[] args) {
        new Win();
    }

    public Win() {
        super("子空间");
        setBounds(0, 0, 500, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        getContentPane().add(jPanel);

        confJsonJFileChooser = new JFileChooser();
        confJsonJFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        confJsonJFileChooser.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confJsonFile = confJsonJFileChooser.getSelectedFile();
                        System.out.println("选择配置文件:" + confJsonFile);
                        setTitle("选择配置文件:" + confJsonFile);
                    }
                });

        confJsonJButton = new JButton("选择配置文件");
        jPanel.add(confJsonJButton);
        confJsonJButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confJsonJFileChooser.showDialog(new JLabel(), "选择配置文件");
                    }
                });

        JLabel countJLabel = new JLabel("循环次数：");
        jPanel.add(countJLabel);

        countJTextField = new JTextField(4);
        countJTextField.setText("1");
        jPanel.add(countJTextField);

        startJButton = new JButton("开始");
        jPanel.add(startJButton);
        startJButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("开始计算");
                        setTitle("开始计算");
                        Main.main(confJsonFile, Integer.valueOf(countJTextField.getText()));
                        System.out.println("计算完成");
                        setTitle("计算完成");
                    }
                });


        setVisible(true);
    }

}
