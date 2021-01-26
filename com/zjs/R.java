package com.zjs;

import com.csvreader.CsvReader;
import com.zjs.ui.LoadingFrame;
import com.zjs.ui.MainFrame;
import com.zjs.ui.MenuPane;
import com.zjs.ui.VerifyPane;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 静态资源类
 * @author zjs
 * */
public class R {
    public static final String version="v2.2";
    public static ScheduledThreadPoolExecutor exec;
    public static MainFrame M;

    //图片资源
    public static BufferedImage compete;
    public static BufferedImage menu;
    public static BufferedImage board;
    public static BufferedImage icon;
    /**问题资源*/
    public static ArrayList<Question> questions;
    //音效资源
    public static Clip button,correct,wrong,countdown,countdownFinal;
    //背景音乐资源
    public static File menuMusic=new File("data\\music\\menu.mp3");
    public static File competeMusic=new File("data\\music\\compete.mp3");
    public static File configFile=new File("data\\config.properties");
    /**配置文件资源*/
    public static Properties config;
    public static Font F=new Font("Microsoft YaHei",Font.BOLD,20);
    /**按键代码到玩家编号的映射，为了防止使用大型switch*/
    public static HashMap<Integer,Integer> playerMap;
    /**按键代码到选项代码的映射，为了防止使用大型switch*/
    public static HashMap<Integer,String> choiceMap;
    /**玩家1名字*/
    public static String p1="玩家1";
    /**玩家2名字*/
    public static String p2="玩家2";
    /**玩家3名字*/
    public static String p3="玩家3";
    /**是否正在使用csv题库*/
    public static boolean usingCsv=true;


    /**
     * 初始化所有资源类
     * @author zjs
     * */
    public static void initResources(){
        try{
            icon=ImageIO.read(new File("data\\images\\icon.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
        LoadingFrame loading=new LoadingFrame();
        loading.setVisible(true);
        exec=new ScheduledThreadPoolExecutor(2);
        //为了避免使用大型switch而使用map映射。
        playerMap=new HashMap<>();
        choiceMap=new HashMap<>();
        playerMap.put(KeyEvent.VK_Q,1);
        playerMap.put(KeyEvent.VK_W,1);
        playerMap.put(KeyEvent.VK_A,1);
        playerMap.put(KeyEvent.VK_S,1);
        playerMap.put(KeyEvent.VK_T,2);
        playerMap.put(KeyEvent.VK_Y,2);
        playerMap.put(KeyEvent.VK_G,2);
        playerMap.put(KeyEvent.VK_H,2);
        playerMap.put(KeyEvent.VK_O,3);
        playerMap.put(KeyEvent.VK_P,3);
        playerMap.put(KeyEvent.VK_L,3);
        playerMap.put(KeyEvent.VK_SEMICOLON,3);
        choiceMap.put(KeyEvent.VK_Q,"a");
        choiceMap.put(KeyEvent.VK_W,"b");
        choiceMap.put(KeyEvent.VK_A,"c");
        choiceMap.put(KeyEvent.VK_S,"d");
        choiceMap.put(KeyEvent.VK_T,"a");
        choiceMap.put(KeyEvent.VK_Y,"b");
        choiceMap.put(KeyEvent.VK_G,"c");
        choiceMap.put(KeyEvent.VK_H,"d");
        choiceMap.put(KeyEvent.VK_O,"a");
        choiceMap.put(KeyEvent.VK_P,"b");
        choiceMap.put(KeyEvent.VK_L,"c");
        choiceMap.put(KeyEvent.VK_SEMICOLON,"d");

        questions=new ArrayList<>();

        try{
            compete= ImageIO.read(new File("data\\images\\compete.png"));
            menu=ImageIO.read(new File("data\\images\\menu.png"));
            board=ImageIO.read(new File("data\\images\\board.png"));
            button=createClip("data\\music\\button.wav");
            correct=createClip("data\\music\\correct.wav");
            wrong=createClip("data\\music\\wrong.wav");
            countdown=createClip("data\\music\\countdown.wav");
            countdownFinal=createClip("data\\music\\countdown_final.wav");
            File f=new File("data\\questions.csv");
            if(f.exists()){
                usingCsv=true;
                loadCsv();
            }else{
                f=new File("data\\questions.txt");
                if(f.exists()) {
                    usingCsv=false;
                    loadTxt();
                }else{
                    JOptionPane.showMessageDialog(loading,"找不到题库文件\r\n请确认\"data\\question.csv\"或\"data\\question.txt\"存在。","找不到题库",JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(loading,"若您看到此窗口，请将此窗口截屏并联系开发者。\r\n"+getStackTrace(e),"加载资源文件时出错",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        config=new Properties();
        initConfig();
        if(!configFile.exists())
            storeConfig();
        M=new MainFrame();
	M.switchTo(MenuPane.class);
        loading.setVisible(false);
        M.setVisible(true);
        //[密码验证部分]
    }
    private static void loadTxt() throws Exception{
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(new FileInputStream("data\\questions.txt"), StandardCharsets.UTF_8));
        String tmp;
        Question q=null;
        int counter=0;
        while((tmp=br.readLine())!=null){
            if(tmp.equalsIgnoreCase("")){
                questions.add(q);
                counter=0;
                continue;
            }
            switch (counter){
                case 0:
                    q=new Question();
                    q.answer=tmp;
                    break;
                case 1:
                    q.q=tmp;
                    break;
                case 2:
                    q.a=tmp;
                    break;
                case 3:
                    q.b=tmp;
                    break;
                case 4:
                    q.c=tmp;
                    break;
                case 5:
                    q.d=tmp;
                    break;
                default:
                    throw new IllegalArgumentException("Failed to parse question file.");
            }
            counter++;
        }
        questions.add(q);
    }
    private static void loadCsv() throws Exception{
        CsvReader reader=new CsvReader("data\\questions.csv",',',StandardCharsets.UTF_8);
        reader.readHeaders();
        String[] headers=reader.getHeaders();
        while(reader.readRecord()){
            Question q=new Question();
            q.q=reader.get(headers[1]);
            int a_num=Integer.parseInt(reader.get(headers[2]));
            q.answer=Character.toString((char)('a'-1+Integer.parseInt(reader.get(headers[3]))));
            q.a=reader.get(headers[4]);
            q.b=reader.get(headers[5]);
            if(a_num>2){
                q.c=reader.get(headers[6]);
                if(a_num>3)
                    q.d=reader.get(headers[7]);
            }
            questions.add(q);
        }
    }
    /**
     * 播放音效。
     * @author zjs
     * */
    public static void playSound(Clip fx){
        fx.stop();
        fx.setFramePosition(0);
        fx.start();
    }
    /**
     * 载入默认配置。
     * @author zjs
     * */
    public static void initConfig(){
        config.put("player1", "玩家1");
        config.put("player2", "玩家2");
        config.put("player3", "玩家3");
        config.put("question_time", "30");
        config.put("interval_time", "5");
        config.put("question_number", "10");
    }
    /**
     * 将配置文件写入文件系统。
     * @author zjs
     * */
    public static void storeConfig(){
        try {
            OutputStreamWriter oos = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8);
            oos.write("# 抢答系统配置文件 v2.0\r\n" +
                    "# player1,player2,player3的值为三位玩家的姓名，将会在抢答过程中和排行榜中显示。\r\n" +
                    "# question_time的值应为正整数，表示每个问题的倒计时时间。\r\n" +
                    "# interval_time的值应为正整数，表示每两个问题之间的等待时间\r\n" +
                    "# question_number的值应为正整数，且小于等于题库中的题目数，表示每轮抢答随机抽取的题目数\r\n" +
                    "# 本配置文件会在每轮抢答开始时重新载入。\r\n" +
                    "# 如果您想要恢复默认设置，删除此文件即可。\r\n");
            oos.flush();
            config.store(oos, null);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 创建某个音效文件的Clip。
     * 只能使用wav类型的文件。
     * @author zjs
     * */
    private static Clip createClip(String file)throws Exception{
        Clip c=AudioSystem.getClip();
        c.open(AudioSystem.getAudioInputStream(new File(file)));
        FloatControl control=(FloatControl)c.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(-12f);
        return c;
    }
    /**
     * 获取错误的stack trace字符串。
     * @author zjs
     * */
    public static String getStackTrace(Throwable t){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
