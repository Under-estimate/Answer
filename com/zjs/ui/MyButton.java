package com.zjs.ui;

import com.zjs.R;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 一个好看的按钮。
 * @author zjs
 * */
public class MyButton extends JPanel {
    private JLabel label;
    private final String text;
    private ActionListener listener;
    public MyButton(String text){
        super();
        this.text=text;
        setBackground(Color.BLACK);
        setBorder(new LineBorder(Color.LIGHT_GRAY));
        setLayout(new BorderLayout());
        initComponents();
    }
    /**
     * 初始化组件
     * @author zjs
     * */
    private void initComponents(){
        label=new JLabel(text,JLabel.CENTER);
        label.setFont(R.F.deriveFont(30f));
        label.setForeground(Color.WHITE);
        add(label,BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(Color.LIGHT_GRAY);
                label.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
                //在这里执行按钮被点击的效果，因为如果在mouseClicked中执行点击效果的话，当鼠标按下和释放的位置有细微差别时mouseClicked不会被调用。
                listener.actionPerformed(null);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.BLACK);
            }
        });
    }
    /**
     * 设置该按钮的事件监听器。当鼠标点击按钮时调用 。
     * 传入的actionEvent为null。
     * @author zjs
     * */
    public void setActionListener(ActionListener l){
        this.listener=l;
    }
    /**
     * 设置该按钮的文本。
     * @author zjs
     * */
    public void setText(String text){
        label.setText(text);
    }
}
