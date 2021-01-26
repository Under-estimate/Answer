package com.zjs.ui;

import com.zjs.R;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
/**
 * 主菜单内容面板。
 * @author zjs
 * */
public class MenuPane extends JPanel {
    public MenuPane(){
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
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    System.exit(0);
                }
                R.playSound(R.button);
                try {
                    if (!R.configFile.exists()) {
                        R.initConfig();
                    }
                    R.config.load(new InputStreamReader(new FileInputStream(R.configFile), StandardCharsets.UTF_8));
                }catch (Exception ex){
                    R.M.showWarningMessage("配置文件格式不正确或无法读取。\r\n请检查配置文件，或删除配置文件以使用默认配置。", "配置文件不正确");
                    return;
                }
                String[] properties={"player1","player2","player3","question_time","interval_time","question_number"};
                String[] integerVal={"question_time","interval_time","question_number"};
                for(String s:properties) {
                    if (!R.config.containsKey(s)) {
                        R.M.showWarningMessage("配置文件中缺少属性:\"" + s + "\"。\r\n请检查配置文件，或删除配置文件以使用默认配置。", "配置文件不正确");
                        return;
                    }
                }
                for(String s:integerVal){
                    try{
                        int i=Integer.parseInt(R.config.getProperty(s));
                        if(i<=0){
                            R.M.showWarningMessage("配置文件中\""+s+"\"的值不是正整数。\r\n请检查配置文件，或删除配置文件以使用默认配置。", "配置文件不正确");
                            return;
                        }
                    }catch (Exception ex){
                        R.M.showWarningMessage("配置文件中\""+s+"\"的值不是整数。\r\n请检查配置文件，或删除配置文件以使用默认配置。", "配置文件不正确");
                        return;
                    }
                }
                if(Integer.parseInt(R.config.getProperty("question_number"))>R.questions.size()) {
                    R.M.showWarningMessage("配置文件中\"question_number\"的值超出了题库中的总题数。\r\n请检查配置文件。\r\n若您在启动程序后更新了题库，请重新启动程序来加载新的题库。", "配置文件不正确");
                    return;
                }
                R.M.getContent(CountDownPane.class).reInit();
                R.M.switchTo(CountDownPane.class);
                R.p1=R.config.getProperty("player1");
                R.p2=R.config.getProperty("player2");
                R.p3=R.config.getProperty("player3");
                R.exec.execute(R.M.getContent(CountDownPane.class)::launch);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }
    /**
     * 重写paint方法以实现绘制背景图片。
     * @author zjs
     * */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawImage(R.menu,0,0,getWidth(),getHeight(),this);
        g2.setColor(Color.white);
        g2.setFont(R.F);
        g2.drawString("使用题库：questions."+(R.usingCsv?"csv":"txt"),10,getHeight()-20);
        g2.drawString("版本号："+R.version,10,getHeight()-50);
        super.paint(g);
    }
}
