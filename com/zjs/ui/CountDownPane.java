package com.zjs.ui;

import com.zjs.R;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 显示开始前的倒计时的内容面板。
 * @author zjs
 * */
public class CountDownPane extends JPanel {
    /**倒计时图片*/
    private final BufferedImage[] countdown=new BufferedImage[5];
    /**当前到了哪一个数字*/
    private int count=5;
    public CountDownPane(){
        super();
        initComponents();
    }
    /**
     * 初始化组件
     * @author zjs
     * */
    private void initComponents(){
        setLayout(null);
        setBackground(Color.BLACK);
        try {
            for (int i = 0; i < 5; i++) {
                countdown[i] = ImageIO.read(new File("data\\images\\" + (i+1) + ".png"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 重新初始化此内容面板。
     * 此方法应在切换到此内容面板之前调用。
     * @author zjs
     * */
    public void reInit(){
        count=5;
    }
    /**
     * 开始执行倒计时过程，倒计时结束后自动切换到抢答内容面板并开始抢答过程。
     * 此方法为阻塞方法，请新建线程执行或使用线程池执行。
     * @author zjs
     * */
    public void launch(){
        for(count=5;count>0;count--){
            repaint();
            R.playSound(R.countdown);
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        R.playSound(R.countdownFinal);
        R.M.switchTo(CompetitionPane.class);
        R.M.getContent(CompetitionPane.class).launch();
    }
    /**
     * 覆盖原有paint方法以绘制倒计时图片。
     * 绘制图片的大小为(屏幕高*屏幕高)。当屏幕宽度小于高度时可能无法正常显示。
     * @author zjs
     * */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        int w=getWidth();
        int h=getHeight();
        g2.drawImage(countdown[count-1],(w-h)/2,0,h,h,this);
    }
}
