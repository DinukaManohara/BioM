package com.conceptscorp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Authenticator extends JFrame implements KeyListener, ActionListener
{
    JLabel displayArea;
    JTextArea typingArea;
    JButton btnAdd;

    private String promptText = "the quick brown fox jumps over the lazy dog";
    private char[] promptChar;
    private Regressor linearRegressor;
    private ArrayList<Long> wordMillis = new ArrayList<Long>();

    private long startTime = 0;

    private int kt, kp, kr, lines, user = 0;

    public static void createAndShowGUI(Regressor regressor) {
        //Create and set up the window.
        Authenticator frame = new Authenticator("Authenticator", regressor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.addComponentsToPane();


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void addComponentsToPane() {

        btnAdd = new JButton("Authenticate");
        //JButton btnClear = new JButton("Clear");
        btnAdd.addActionListener(this);
        btnAdd.setEnabled(false);

        typingArea = new JTextArea();
        typingArea.setEditable(true);
        typingArea.addKeyListener(this);

        //Uncomment this if you wish to turn off focus
        //traversal.  The focus subsystem consumes
        //focus traversal keys, such as Tab and Shift Tab.
        //If you uncomment the following line of code, this
        //disables focus traversal and the Tab events will
        //become available to the key event listener.
        //typingArea.setFocusTraversalKeysEnabled(false);

        displayArea = new JLabel("the quick brown fox jumps over the lazy dog");
        JScrollPane scrollPane = new JScrollPane(typingArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));

        getContentPane().add(displayArea, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(btnAdd, BorderLayout.PAGE_END);
        //getContentPane().add(btnClear, BorderLayout.PAGE_END);
    }

    public Authenticator(String name, Regressor regressor) {
        super(name);
        this.promptChar = promptText.toCharArray();
        this.linearRegressor = regressor;
        this.wordMillis.add((long)1);
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        //new DataThread(e, this.dataPool, promptChar[kt], System.nanoTime()).start();
        if(promptChar[kt] == e.getKeyChar()) {
            this.kt += 1;
        }
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        if(10 == e.getKeyCode()) {
            this.kt = 0;
            this.kp = 0;
            this.kr = 0;
            this.lines += 1;
            if(this.lines == 1){
                //typingArea.setText("");
                //this.lines = 0;
                //this.user += 1;
                /*if(this.user == 2){
                    btnAdd.setEnabled(true);
                    typingArea.setEnabled(false);
                }*/
                btnAdd.setEnabled(true);
                typingArea.setEnabled(false);
            }
        }
        //new DataThread(e, this.dataPool, promptChar[kp], System.nanoTime()).start();
        if(promptChar[kp] == e.getKeyChar()) {
            if(kp == 0 || kp == 4 || kp == 10 || kp == 16 || kp == 20 || kp == 26 || kp == 31 || kp == 35 || kp == 40) {
                this.startTime = System.currentTimeMillis();
            }
            this.kp += 1;
        }
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        //new DataThread(this.dataPool, promptChar[kr], System.nanoTime()).start();
        if(promptChar[kr] == e.getKeyChar()) {
            if(kr == 2 || kr == 8 || kr == 14 || kr == 18 || kr == 24 || kr == 29 || kr == 33 || kr == 38 || kr == 42){
                long wordTime = System.currentTimeMillis() - this.startTime;
                this.wordMillis.add(wordTime);
            }
            this.kr += 1;
        }
    }

    /** Handle the button click. */
    public void actionPerformed(ActionEvent e) {
        //this.dataPool.printAll();
        System.out.println("User : " + this.linearRegressor.identifyUser(this.wordMillis));
        this.wordMillis.clear();
        typingArea.setEnabled(true);
        typingArea.setText("");
    }
}
