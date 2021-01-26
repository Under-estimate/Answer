package com.zjs.ui;

import com.zjs.R;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

/**
 * 主窗体。
 * @author zjs
 * */
public class MainFrame extends JFrame{
    /**所有内容面板*/
    public HashMap<Class<? extends JPanel>,JPanel> content;
    /**内容面板切换管理器*/
    private CardLayout cardManager;
    /**当前正在播放背景音乐的player，用于切换背景音乐时结束上一个背景音乐*/
    private Player currentBgm=null;
    /**当前正在播放的背景音乐文件*/
    private File playing=null;
    public MainFrame(){
        super("抢答系统 "+R.version+" by zjs");
        initComponents();
    }
    /**
     * 初始化组件。
     * @author zjs
     * */
    private void initComponents() {
        content=new HashMap<>();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        cardManager=new CardLayout();
        setLayout(cardManager);
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screen.width,screen.height);
        content.put(CompetitionPane.class,new CompetitionPane());
        content.put(MenuPane.class,new MenuPane());
        content.put(LeadBoardPane.class,new LeadBoardPane());
        content.put(VerifyPane.class,new VerifyPane());
        content.put(CountDownPane.class,new CountDownPane());
        add(getContent(CompetitionPane.class),CompetitionPane.class.getName());
        add(getContent(MenuPane.class),MenuPane.class.getName());
        add(getContent(LeadBoardPane.class),LeadBoardPane.class.getName());
        add(getContent(VerifyPane.class),VerifyPane.class.getName());
        add(getContent(CountDownPane.class),CountDownPane.class.getName());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayouts();
            }
        });
        setIconImage(R.icon);
    }
    /**
     * 切换到指定的内容面板。
     * 切换到某些面板会自动更换背景音乐。
     * @author zjs
     * */
    public void switchTo(Class<? extends JPanel> card){
        cardManager.show(getContentPane(),card.getName());
        if(card==MenuPane.class||card==LeadBoardPane.class){
            if(playing!=R.menuMusic) {
                if (currentBgm != null)
                    currentBgm.close();
                playing = R.menuMusic;
                R.exec.execute(() -> {
                    while(playing==R.menuMusic) {
                        try {
                            currentBgm = new Player(new FileInputStream(R.menuMusic));
                            currentBgm.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else if(card==CompetitionPane.class){
            if(playing!=R.competeMusic) {
                if (currentBgm != null)
                    currentBgm.close();
                playing = R.competeMusic;
                R.exec.execute(() -> {
                    while(playing==R.competeMusic) {
                        try {
                            currentBgm = new Player(new FileInputStream(R.competeMusic));
                            currentBgm.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }else{
            if(currentBgm!=null)
                currentBgm.close();
            playing=null;
            currentBgm=null;
        }
        revalidate();
        getContent(card).requestFocusInWindow();
    }
    /**
     * 获取指定的内容面板
     * @see MainFrame#content
     * @author zjs
     * */
    public <T> T getContent(Class<T> target){
        return target.cast(content.get(target));
    }
    /**
     * 更新所有内容面板的组件布局。
     * 只有含有内部组件的内容面板需要在窗口大小改变时更新布局。
     * TODO:此函数是之前为了能在窗口大小改变时更新组件布局而设置的，现在窗口默认全屏，不需要再更新组件布局。将来请考虑删除此函数。
     * @see CompetitionPane#updateLayout()
     * @see VerifyPane#updateLayout()
     * @author zjs
     * */
    public void updateLayouts(){
        R.M.getContent(CompetitionPane.class).updateLayout();
        R.M.getContent(VerifyPane.class).updateLayout();
    }
    /**
     * 显示以此窗口为父级窗口的警告框。
     * 为了简便。
     * @author zjs
     * */
    public void showWarningMessage(String message,String title){
        JOptionPane.showMessageDialog(this,message,title,JOptionPane.WARNING_MESSAGE);
    }
}
