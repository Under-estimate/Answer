package com.zjs.ui;

import com.zjs.R;

import javax.swing.*;
import java.awt.*;

/**
 * 提示加载中的窗体。
 * @author zjs
 * */
public class LoadingFrame extends JFrame {
    public LoadingFrame(){
        super("加载中...");
        initComponents();
    }
    private void initComponents(){
        setUndecorated(true);
        setIconImage(R.icon);
        setLayout(new BorderLayout());
        Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((d.width-300)/2,(d.height-100)/2,300,100);
        JLabel l=new JLabel("   抢答系统启动中，请稍侯...");
        l.setFont(R.F);
        add(new JPanel(),BorderLayout.NORTH);
        add(l,BorderLayout.CENTER);
        JProgressBar prog=new JProgressBar();
        prog.setIndeterminate(true);
        add(prog,BorderLayout.SOUTH);
    }
}
