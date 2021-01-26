package com.zjs.ui;

import com.zjs.R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * 排行榜内容面板
 * @author zjs
 * */
public class LeadBoardPane extends JPanel {
    /**返回主菜单的按钮*/
    private MyButton btn;
    /**返回主菜单倒计时的过程*/
    private Future<?> backFuture=null;
    public LeadBoardPane(){
        super();
        initComponents();
    }
    /**
     * 初始化组件
     * @author zjs
     * */
    private void initComponents(){
        setLayout(null);
        setOpaque(false);
        btn=new MyButton("返回主菜单");
        btn.setBounds(10,10,400,50);
        btn.setActionListener(e -> {
            R.playSound(R.button);
            R.M.switchTo(MenuPane.class);
            //设为null之后倒计时线程会自动退出
            backFuture=null;
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                R.playSound(R.button);
                R.M.switchTo(MenuPane.class);
                //设为null之后倒计时线程会自动退出
                backFuture=null;
            }
        });
        add(btn);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    /**
     * 重写paint方法以实现绘制背景图片和文字。
     * @author zjs
     * */
    @Override
    public void paint(Graphics g) {
        g.drawImage(R.board,0,0,getWidth(),getHeight(),this);
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.setFont(R.F.deriveFont(Font.BOLD,30));
        int p1=R.M.getContent(CompetitionPane.class).getAttr("1",Integer.class),
                p2=R.M.getContent(CompetitionPane.class).getAttr("2",Integer.class),
                p3=R.M.getContent(CompetitionPane.class).getAttr("3",Integer.class);
        Struct[] st=new Struct[]{new Struct(R.p1,p1),new Struct(R.p2,p2),new Struct(R.p3,p3)};
        Arrays.sort(st);
        g2.drawString(st[0].name,pWidth(0.45),pHeight(0.5));
        g2.drawString(st[1].name,pWidth(0.45),pHeight(0.65));
        g2.drawString(st[2].name,pWidth(0.45),pHeight(0.8));
        g2.drawString(Integer.toString(st[0].value),pWidth(0.65),pHeight(0.5));
        g2.drawString(Integer.toString(st[1].value),pWidth(0.65),pHeight(0.65));
        g2.drawString(Integer.toString(st[2].value),pWidth(0.65),pHeight(0.8));
    }
    /**
     * 执行返回主菜单的倒计时。
     * 非阻塞方法。
     * 执行该方法会将backFuture设为创建的Future，当backFuture为null时，线程的运行会在1s内结束。
     * @see LeadBoardPane#backFuture
     * @author zjs
     * */
    public void launch(){
        backFuture=R.exec.submit(()->{
            for(int i=30;i>0;i--){
                btn.setText("按任意键返回主菜单("+i+")");
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(backFuture==null)
                    return;
            }
            R.M.switchTo(MenuPane.class);
        });
    }
    /**
     * 将比例宽度转换为绝对宽度。
     * @author zjs
     * */
    private int pWidth(double d){
        return (int)(d*getWidth());
    }
    /**
     * 将比例高度转换为绝对高度。
     * @author zjs
     * */
    private int pHeight(double d){
        return (int)(d*getHeight());
    }
    /**
     * 为了方便排序得分高低而设计的类。
     * @author zjs
     * */
    private static class Struct implements Comparable<Struct>{
        public Struct(String name,int value){
            this.name=name;
            this.value=value;
        }
        String name;
        int value;
        @Override
        public int compareTo(Struct o) {
            return o.value-value;
        }
    }
}
