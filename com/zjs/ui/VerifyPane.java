package com.zjs.ui;

import com.zjs.R;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Scanner;

public class VerifyPane extends JPanel {
    private JTextArea jta;
    private MyButton btn;
    private MyButton exit;
    public String hint="输入许可证号以在本设备上使用此软件";
    public VerifyPane(){
        super();
        initComponents();
    }
    private void initComponents(){
        setBackground(Color.BLACK);
        setLayout(null);
        jta=new JTextArea();
        jta.setBackground(Color.BLACK);
        jta.setForeground(Color.WHITE);
        jta.setCaretColor(Color.WHITE);
        jta.setBorder(new LineBorder(Color.WHITE));
        jta.setFont(R.F);
        btn=new MyButton("确定");
        btn.setActionListener(e -> {
            //[密码验证部分]
        });
        add(jta);
        add(btn);
        exit=new MyButton("退出");
        exit.setActionListener(e -> System.exit(0));
        add(exit);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.YELLOW);
        g2.setFont(R.F);
        g2.drawString(hint,(getWidth()-300)/2,150);
    }

    public void updateLayout(){
        jta.setBounds((getWidth()-600)/2,getHeight()-400,600,100);
        btn.setBounds((getWidth()-500)/2,getHeight()-200,500,70);
        exit.setBounds((getWidth()-500)/2,getHeight()-120,500,70);
    }
}
