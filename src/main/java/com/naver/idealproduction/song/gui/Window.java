package com.naver.idealproduction.song.gui;

import com.naver.idealproduction.song.gui.panel.Console;
import com.naver.idealproduction.song.servlet.service.OverlayService;
import com.naver.idealproduction.song.servlet.service.SimTracker;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.*;

public class Window extends JFrame {
    private SimTracker simTracker;
    private JTabbedPane contentPane;

    public void start(
            Console console,
            SimTracker simTracker,
            ConfigurableApplicationContext context
    ) {
        this.simTracker = simTracker;
        contentPane = new JTabbedPane();
        var dashboard = new Dashboard(context);
        var overlayPanel = new Overlays(this, context.getBean(OverlayService.class));
        contentPane.addTab("Dashboard", dashboard);
        contentPane.addTab("Overlays", overlayPanel);
        contentPane.addTab("Console", console);
        setContentPane(contentPane);
        setResizable(false);
        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("SimOverlayNG");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * @param type Magic value: JOptionPane.XXX
     * @param message Message to display
     */
    public void showDialog(int type, String message) {
        String title = "Message";
        switch (type) {
            case PLAIN_MESSAGE,
                    INFORMATION_MESSAGE,
                    QUESTION_MESSAGE -> title = "Message";
            case WARNING_MESSAGE -> title = "Warning";
            case ERROR_MESSAGE -> title = "Error";
        }
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    public JTabbedPane getContentTab() {
        return contentPane;
    }

    @Override
    public void dispose() {
        super.dispose();
        simTracker.terminate();
    }
}
