package com.codexo;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.add(new GamePlay());

        frame.setTitle("Snake Feeder");
        frame.setBounds(20, 10, 1000, 600);
        frame.setBackground(Color.BLACK);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
