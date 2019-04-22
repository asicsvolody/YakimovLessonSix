package ru.yakimovvn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

class Frame extends JFrame {

    private JButton display;
    private JLayeredPane layeredPane;
    private double result=0;
    private boolean start=true;
    private int symbolNumber=1;
    private char lastCommand='=';

    Frame(){
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10,10,304,510);
        setResizable(false);
        setBackground(Color.BLACK);

        layeredPane = getLayeredPane();
        layeredPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        layeredPane.setBackground(Color.BLACK);

        addDisplay();

        ActionListener numberAct=new NumberAction();
        ActionListener command = new CommandAction();

        addButton('C',command,KeyEvent.VK_BACK_SPACE);
        addButton('%',command,KeyEvent.VK_5);
        addButton('^',command,KeyEvent.VK_6);
        addButton('/',command,KeyEvent.VK_SLASH);
        addButton('9',numberAct,KeyEvent.VK_9);
        addButton('8',numberAct,KeyEvent.VK_8);
        addButton('7',numberAct,KeyEvent.VK_7);
        addButton('+',command,KeyEvent.VK_EQUALS);
        addButton('6',numberAct,KeyEvent.VK_6);
        addButton('5',numberAct,KeyEvent.VK_5);
        addButton('4',numberAct,KeyEvent.VK_4);
        addButton('-',command,KeyEvent.VK_MINUS);
        addButton('3',numberAct,KeyEvent.VK_3);
        addButton('2',numberAct,KeyEvent.VK_2);
        addButton('1',numberAct,KeyEvent.VK_1);
        addButton('*',command,KeyEvent.VK_8);
        addButton('0',numberAct,KeyEvent.VK_0);
        addButton('.',numberAct,KeyEvent.VK_PERIOD);
        addButton('±',command,KeyEvent.VK_M);
        addButton('=',command,KeyEvent.VK_ENTER);

        setVisible(true);
    }

    private void addDisplay(){
        display=new JButton("0");
        display.setPreferredSize(new Dimension(getSize().width-10,getSize().height/5));
        display.setOpaque(true);
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        Font fontDisplay=new Font("TimesRoman",Font.BOLD,30);
        display.setFont(fontDisplay);

        display.setForeground(Color.BLACK);
        display.setEnabled(false);

        layeredPane.add(display,JLayeredPane.POPUP_LAYER);
    }

    private void addButton(char symbol,ActionListener listener,int kay) {
        JButton button = new JButton("" + symbol);

        Font fontButton = new Font("TimesRoman", Font.BOLD, 30);
        button.setFont(fontButton);

        button.setActionCommand("" + symbol);
        button.setPreferredSize(new Dimension(70, 70));
        button.addActionListener(listener);

        clickOnKey(button, "" + symbol, kay);

        button.setBackground(Color.PINK);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        layeredPane.add(button,JLayeredPane.POPUP_LAYER);
    }

    private static void clickOnKey(
        final AbstractButton button, String actionName, int key ){
            if(actionName.equals("+")||actionName.equals("*")||actionName.equals("%")||actionName.equals("^")){
                button.getInputMap( JButton.WHEN_IN_FOCUSED_WINDOW )
                        .put( KeyStroke.getKeyStroke( key, KeyEvent.SHIFT_DOWN_MASK ), actionName );
            }
            else {
                button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW)
                        .put(KeyStroke.getKeyStroke(key, 0), actionName);
            }

            button.getActionMap().put( actionName, new AbstractAction()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    button.doClick();
                }
            } );
    }

    private class NumberAction  implements ActionListener{
        public void actionPerformed (ActionEvent event){
            if(start){
                switch (event.getActionCommand().charAt(0)){
                    case '.':
                        display.setText("0");
                        start=false;
                        symbolNumber++;
                        break;
                    case '0' :
                        result=0;
                        lastCommand='=';
                        display.setText("");
                        start=true;
                        break;
                    default:
                        display.setText("");
                        start = false;
                }
            }
            if(symbolNumber<=17){
                display.setText(display.getText()+event.getActionCommand());
                symbolNumber++;
            }
        }
    }

    private class CommandAction implements ActionListener{
        public void actionPerformed(ActionEvent event){
            char command=event.getActionCommand().charAt(0);

            if(command=='C') {
                result=0;
                lastCommand='=';
                display.setText("0");
                start=true;
            }
            else if (command=='±') display.setText(formatResult(""+(Double.parseDouble(display.getText())*(-1))));
            else if (command=='%') display.setText(formatResult(""+((result*Double.parseDouble(display.getText()))/100)));
            else if (start){
                if(command=='-'){
                    display.setText("-");
                    start=false;
                }
                else lastCommand=command;
            }
            else {

                calculator(Double.parseDouble(display.getText()));
                lastCommand=command;
                start=true;
                symbolNumber=1;
            }
        }
    }

    private void calculator(double x){
        switch (lastCommand){
            case '+':
                result+=x;
                break;
            case '-':
                result-=x;
                break;
            case '/':
                result/=x;
                break;
            case '*':
                result*=x;
                break;
            case '^':
                double y=result;
                x=(int)x;
                if(x==0)result=1;
                else if(x==-1)result=1/result;
                else if(x<-1){
                    for (int i = 1; i <=(x*(-1)); i++) {
                        result*=y;
                    }
                    result=1/result;
                }
                else {

                    for (int i = 2; i <= x; i++) {
                        result *= y;
                    }
                }
                break;
            case '√' :
                if(result<0||x<0){
                    JOptionPane.showMessageDialog(null,"Невозможно извлечь степень из отрицательного числа\n" +
                            "Невозмложно извлечь корень отрицытельной степени","Ошибка!!!",JOptionPane.ERROR_MESSAGE);
                    result=0;
                    lastCommand='=';
                    display.setText("0");
                    start=true;
                    symbolNumber=1;
                }else {
                    result=Math.pow(result,1/x);
                }
                break;
            case '=':
                result=x;

        }
        display.setText(formatResult(""+result));
    }

    private String formatResult(String result){
        int lengthDisplay=17;

        String[] resultArray=result.split("\\.");
        if (resultArray[0].length()>lengthDisplay){
            JOptionPane.showMessageDialog(this,"Число не может быть отображено на дисплее", "Ошибка",JOptionPane.ERROR_MESSAGE);
            return "ERR";
        }
      else if (resultArray[1].equals("0")) return resultArray[0];
        else{
            if((resultArray[0].length()+resultArray[1].length())>(lengthDisplay-1)){
                int lastSymbol=9-resultArray[0].length();
                result=resultArray[0]+"."+resultArray[1].substring(0,lastSymbol);
            }
            else{
                result=resultArray[0]+"."+resultArray[1];
            }
            return result;
        }
    }
}
