package com.zjs.ui;

import com.zjs.Question;
import com.zjs.R;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 抢答内容面板。
 * @author zjs
 * */
public class CompetitionPane extends JPanel {
    /**
     * 属性存储器，包含所有变量。
     * @see CompetitionPane#setAttr(String, Object)
     * @see CompetitionPane#getAttr(String, Class)
     * */
    private final HashMap<String,Object> attr=new HashMap<>();
    public CompetitionPane(){
        super();
        initComponents();
    }
    /**
     * 初始化组件
     * @author zjs
     * */
    private void initComponents(){
        setOpaque(false);
        setLayout(null);
        reInit();
        JTextArea tmp=createTextArea();
        tmp.setFont(R.F.deriveFont(Font.BOLD,25));
        setAttr("q",tmp);
        add(tmp);
        tmp=createTextArea();
        setAttr("a",tmp);
        add(tmp);
        tmp=createTextArea();
        setAttr("b",tmp);
        add(tmp);
        tmp=createTextArea();
        setAttr("c",tmp);
        add(tmp);
        tmp=createTextArea();
        setAttr("d",tmp);
        add(tmp);
        //读取用户按下的按键并执行有关过程
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!R.playerMap.containsKey(e.getKeyCode())||!getAttr("compete",Boolean.class))
                    return;
                if(getAttr(R.choiceMap.get(e.getKeyCode()),JTextArea.class).getText().equalsIgnoreCase(""))
                    return;
                int answerer=R.playerMap.get(e.getKeyCode());
                boolean isCorrect=R.choiceMap.get(e.getKeyCode())
                        .equalsIgnoreCase(getAttr("answer",String.class));
                setAttr("compete",false);
                setAttr("summary",true);
                setAttr("answerer",answerer);
                setAttr("isCorrect",isCorrect);
                setAttr(Integer.toString(answerer),getAttr(Integer.toString(answerer),Integer.class)+(isCorrect?100:(-100)));
                if(!isCorrect){
                    R.playSound(R.wrong);
                    setAttr("wrong",R.choiceMap.get(e.getKeyCode()));
                }else{
                    R.playSound(R.correct);
                }
                R.exec.execute(CompetitionPane.this::executeSummary);
            }
        });
        //当鼠标移入组件时请求焦点，否则无法读取键盘事件
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        updateTextAreas();
    }
    /**
     * 重写paint方法以实现除问题和选项文本以外的文本和图像绘制。
     * @author zjs
     * */
    @Override
    public void paint(Graphics g){
        Graphics2D g2=(Graphics2D)g;
        //绘制背景图片
        g2.drawImage(R.compete,0,0,getWidth(),getHeight(),this);
        //绘制半透明黑色遮罩
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0,0,0,200));
        g2.fillRoundRect(50,20,300,50,50,50);
        g2.fillRoundRect(pWidth(0.1),pHeight(0.15),pWidth(0.8),pHeight(0.65),50,50);
        g2.fillOval(getWidth()-100,70,70,70);
        g2.fillRect(pWidth(0.2),getHeight()-120,150,70);
        g2.fillRect(pWidth(0.45),getHeight()-120,150,70);
        g2.fillRect(pWidth(0.7),getHeight()-120,150,70);
        //绘制组件
        super.paint(g);
        //绘制三个白色三角形
        g2.setColor(Color.WHITE);
        g2.setFont(R.F.deriveFont(Font.BOLD,30));
        g2.fillPolygon(new int[]{260,275,260},new int[]{33,43,53},3);
        g2.fillPolygon(new int[]{280,295,280},new int[]{33,43,53},3);
        g2.fillPolygon(new int[]{300,315,300},new int[]{33,43,53},3);
        //绘制静态文本
        g2.drawString("倒计时",getWidth()-120,40);
        g2.drawString(R.p1+" 得分",pWidth(0.2),getHeight()-20);
        g2.drawString(R.p2+" 得分",pWidth(0.45),getHeight()-20);
        g2.drawString(R.p3+" 得分",pWidth(0.7),getHeight()-20);
        g2.drawString("A",pWidth(0.12)+15,pHeight(0.46)+35);
        g2.drawString("B",pWidth(0.5)+15,pHeight(0.46)+35);
        g2.drawString("C",pWidth(0.12)+15,pHeight(0.61)+35);
        g2.drawString("D",pWidth(0.5)+15,pHeight(0.61)+35);
        //绘制动态文本
        g2.drawString(getAttr("title",String.class),85,53);
        g2.drawString(Integer.toString(getAttr("1",Integer.class)),pWidth(0.2)+40,getHeight()-70);
        g2.drawString(Integer.toString(getAttr("2",Integer.class)),pWidth(0.45)+40,getHeight()-70);
        g2.drawString(Integer.toString(getAttr("3",Integer.class)),pWidth(0.7)+40,getHeight()-70);
        g2.drawString(Integer.toString(getAttr("time",Integer.class)),getWidth()-85,115);
        //绘制白色边框
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRect(pWidth(0.2),getHeight()-120,150,70);
        g2.drawRect(pWidth(0.45),getHeight()-120,150,70);
        g2.drawRect(pWidth(0.7),getHeight()-120,150,70);
        g2.drawRoundRect(pWidth(0.12),pHeight(0.46),pWidth(0.37),pHeight(0.13),50,50);
        g2.drawRoundRect(pWidth(0.5),pHeight(0.46),pWidth(0.37),pHeight(0.13),50,50);
        g2.drawRoundRect(pWidth(0.12),pHeight(0.61),pWidth(0.37),pHeight(0.13),50,50);
        g2.drawRoundRect(pWidth(0.5),pHeight(0.61),pWidth(0.37),pHeight(0.13),50,50);
        g2.drawOval(pWidth(0.12),pHeight(0.46),50,50);
        g2.drawOval(pWidth(0.5),pHeight(0.46),50,50);
        g2.drawOval(pWidth(0.12),pHeight(0.61),50,50);
        g2.drawOval(pWidth(0.5),pHeight(0.61),50,50);
        g2.setStroke(new BasicStroke(6f));
        g2.drawOval(getWidth()-100,70,70,70);
        g2.drawRoundRect(pWidth(0.1),pHeight(0.15),pWidth(0.8),pHeight(0.65),50,50);
        //若有需要，覆盖绘制红色/绿色的分数
        if(getAttr("summary",Boolean.class)){
            g2.setColor(getAttr("isCorrect",Boolean.class)?Color.GREEN:Color.RED);
            int answerer=getAttr("answerer",Integer.class);
            if(answerer>0)
                g2.drawString(Integer.toString(getAttr(Integer.toString(answerer),Integer.class)),
                        pWidth(0.25*answerer-0.05)+40,getHeight()-70);
        }
    }
    /**
     * 开始执行抢答过程。
     * 非阻塞方法。
     * @author zjs
     * */
    public void launch(){
        reInit();
        updateTextAreas();
        repaint();
        prepareNextQuestion();
        requestFocusInWindow();
        R.exec.execute(this::executeCompete);
    }
    /**
     * 重新初始化此内容面板。
     * 此方法应在切换到此内容面板之前调用。
     * @author zjs
     * */
    private void reInit(){
        setAttr("wt",Integer.parseInt(R.config.getProperty("interval_time")));
        setAttr("qt",Integer.parseInt(R.config.getProperty("question_time")));
        setAttr("qn",Integer.parseInt(R.config.getProperty("question_number")));
        setAttr("qf",R.F.deriveFont(Font.BOLD,(float)Math.sqrt(getWidth()*getHeight())/40));
        setAttr("af",R.F.deriveFont(Font.BOLD,(float)Math.sqrt(getWidth()*getHeight())/50));
        setAttr("title","准备阶段");
        setAttr("q_text","准备开始...");
        setAttr("a_text", String.format("%s:Q  %s:T  %s:O(英文字母)",R.p1,R.p2,R.p3));
        setAttr("b_text", String.format("%s:W  %s:Y  %s:P",R.p1,R.p2,R.p3));
        setAttr("c_text", String.format("%s:A  %s:G  %s:L",R.p1,R.p2,R.p3));
        setAttr("d_text", String.format("%s:S  %s:H  %s:;(分号键)",R.p1,R.p2,R.p3));
        setAttr("1",0);
        setAttr("2",0);
        setAttr("3",0);
        setAttr("time",30);
        setAttr("answer",null);
        setAttr("compete",false);
        setAttr("summary",false);
        setAttr("answerer",-1);
        setAttr("isCorrect",false);
        setAttr("count",0);
        setAttr("wrong",null);
        //随机抽取问题
        ArrayList<Question> pool=new ArrayList<>(R.questions);
        ArrayList<Question> target=new ArrayList<>();
        for(int i=0;i<Math.min(getAttr("qn",Integer.class),R.questions.size());i++){
            int index=(int)(Math.random()*pool.size());
            target.add(pool.remove(index));
        }
        setAttr("questionIter",target.iterator());
    }
    /**
     * 设置属性内容。
     * @param name 属性名称
     * @param obj 属性内容
     * @see CompetitionPane#attr
     * @author zjs
     * */
    public void setAttr(String name,Object obj){
        attr.put(name,obj);
    }
    /**
     * 获取属性内容。
     * @param name 属性名称
     * @param type 属性类型，为了省去类型转换
     * @see CompetitionPane#attr
     * @author zjs
     * */
    public <T> T getAttr(String name, Class<T> type){
        return type.cast(attr.get(name));
    }
    /**
     * 更新题目和选项文本及颜色。
     * @author zjs
     * */
    public void updateTextAreas(){
        getAttr("q",JTextArea.class).setText(getAttr("q_text",String.class));
        getAttr("a",JTextArea.class).setText(getAttr("a_text",String.class));
        getAttr("b",JTextArea.class).setText(getAttr("b_text",String.class));
        getAttr("c",JTextArea.class).setText(getAttr("c_text",String.class));
        getAttr("d",JTextArea.class).setText(getAttr("d_text",String.class));

        getAttr("q",JTextArea.class).setFont(getAttr("qf",Font.class));
        getAttr("a",JTextArea.class).setFont(getAttr("af",Font.class));
        getAttr("b",JTextArea.class).setFont(getAttr("af",Font.class));
        getAttr("c",JTextArea.class).setFont(getAttr("af",Font.class));
        getAttr("d",JTextArea.class).setFont(getAttr("af",Font.class));
        if(getAttr("summary",Boolean.class)){
            if(getAttr("answer",String.class)!=null)
                getAttr(getAttr("answer",String.class),JTextArea.class).setForeground(Color.GREEN);
            if(getAttr("wrong",String.class)!=null)
                getAttr(getAttr("wrong",String.class),JTextArea.class).setForeground(Color.RED);

        }else{
            getAttr("a",JTextArea.class).setForeground(Color.WHITE);
            getAttr("b",JTextArea.class).setForeground(Color.WHITE);
            getAttr("c",JTextArea.class).setForeground(Color.WHITE);
            getAttr("d",JTextArea.class).setForeground(Color.WHITE);
        }
    }
    /**
     * 更新问题和选项文本的布局
     * @author zjs
     * */
    public void updateLayout(){
        getAttr("q",JTextArea.class).setBounds(transformBounds(0.12,0.17,0.76,0.25));
        getAttr("a",JTextArea.class).setBounds(transformBounds(0.17,0.46,0.3,0.13));
        getAttr("b",JTextArea.class).setBounds(transformBounds(0.55,0.46,0.3,0.13));
        getAttr("c",JTextArea.class).setBounds(transformBounds(0.17,0.61,0.3,0.13));
        getAttr("d",JTextArea.class).setBounds(transformBounds(0.55,0.61,0.3,0.13));
        revalidate();
    }
    /**
     * 执行抢答过程，结束后自动执行总结过程。
     * 阻塞方法，请新建线程或使用线程池调用。
     * 当属性compete为false时，方法会在100ms之内结束。
     * @see CompetitionPane#executeSummary()
     * @author zjs
     * */
    private void executeCompete(){
        setAttr("summary",false);
        setAttr("compete",true);
        updateTextAreas();
        setAttr("time",getAttr("qt",Integer.class));
        OUTER:
        for(int i=0;i<getAttr("qt",Integer.class);i++){
            for(int j=0;j<10;j++) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!getAttr("compete",Boolean.class))
                    break OUTER;
            }
            setAttr("time",getAttr("time",Integer.class)-1);
            repaint();
        }
        if(getAttr("compete",Boolean.class))
            R.exec.execute(this::executeSummary);
    }
    /**
     * 执行总结过程，结束后自动执行抢答过程或跳转到排行榜。
     * 阻塞方法，请新建线程或使用线程池调用。
     * @see CompetitionPane#executeCompete()
     * @author zjs
     * */
    private void executeSummary(){
        setAttr("summary",true);
        setAttr("compete",false);
        updateTextAreas();
        setAttr("time",getAttr("wt",Integer.class));
        repaint();
        for(int i=0;i<getAttr("wt",Integer.class);i++){
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            setAttr("time",getAttr("time",Integer.class)-1);
            repaint();
        }
        if(getAttr("questionIter",Iterator.class).hasNext()) {
            prepareNextQuestion();
            R.exec.execute(this::executeCompete);
        }else{
            R.M.switchTo(LeadBoardPane.class);
            R.M.getContent(LeadBoardPane.class).launch();
        }
    }
    /**
     * 加载下一个问题并存入属性列表。
     * 此方法不检查是否有下一个问题。
     * @author zjs
     * */
    private void prepareNextQuestion(){
        setAttr("count",getAttr("count",Integer.class)+1);
        Question next=(Question)(getAttr("questionIter",Iterator.class).next());
        setAttr("title","第"+getAttr("count",Integer.class)+"题");
        setAttr("q_text",next.q);
        setAttr("a_text",next.a);
        setAttr("b_text",next.b);
        setAttr("c_text",next.c);
        setAttr("d_text",next.d);
        setAttr("time",getAttr("qt",Integer.class));
        setAttr("answer",next.answer);
        setAttr("wrong",null);
    }
    /**
     * 将比例位置转换为绝对位置。
     * @author zjs
     * */
    private Rectangle transformBounds(double x, double y,double w,double h){
        return new Rectangle(pWidth(x),pHeight(y),pWidth(w),pHeight(h));
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
     * 创建指定格式的文本框。
     * @author zjs
     * */
    private static JTextArea createTextArea(){
        JTextArea jta=new JTextArea();
        jta.setLineWrap(true);
        jta.setOpaque(false);
        jta.setBorder(new EmptyBorder(0,0,0,0));
        jta.setForeground(Color.WHITE);
        jta.setEditable(false);
        jta.setSelectionColor(new Color(255,255,255,200));
        jta.setSelectedTextColor(Color.WHITE);
        jta.setCaretColor(Color.WHITE);
        jta.setFont(R.F);
        return jta;
    }
}
