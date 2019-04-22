package ru.yakimovvn;

import javax.swing.*;
import java.awt.*;

public class CalculatorGeekBrains {
    public static void main(String[] args) {
        try {
           UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
           e.printStackTrace();
        }
        EventQueue.invokeLater(()->{
            Frame frame=new Frame();
        });
    }
}
