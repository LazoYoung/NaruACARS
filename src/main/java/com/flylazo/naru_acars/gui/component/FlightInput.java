package com.flylazo.naru_acars.gui.component;

import com.flylazo.naru_acars.domain.Aircraft;
import com.flylazo.naru_acars.gui.Window;
import com.flylazo.naru_acars.servlet.repository.AircraftRepository;
import jakarta.annotation.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

public class FlightInput extends JPanel {
    private final AircraftRepository aircraftRepo;
    private final JTextField csInput;
    private final JTextField acfInput;
    private final JTextField fltTimeInput;
    private final Window window;

    public FlightInput(Window window, Font labelFont) {
        var csLabel = window.bakeLabel("Callsign", labelFont, Color.black);
        var acfLabel = window.bakeLabel("Aircraft", labelFont, Color.black);
        var fltTimeLabel = window.bakeLabel("Flight time", labelFont, Color.black);
        this.window = window;
        this.aircraftRepo = window.getServiceFactory().getBean(AircraftRepository.class);
        this.csInput = new TextInput(7, true);
        this.acfInput = new TextInput(7, true);
        this.fltTimeInput = new TextInput("hh:mm", 7, false);
        this.csInput.getDocument().addDocumentListener(getValidator());
        this.acfInput.getDocument().addDocumentListener(getValidator());
        this.fltTimeInput.getDocument().addDocumentListener(getValidator());
        this.addPropertyChangeListener("enabled", this::onEnabledPropertyChange);

        var layout = new GroupLayout(this);
        var hGroup = layout.createParallelGroup()
                .addComponent(csLabel)
                .addComponent(this.csInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .addComponent(acfLabel)
                .addComponent(this.acfInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .addComponent(fltTimeLabel)
                .addComponent(this.fltTimeInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);
        var vGroup = layout.createSequentialGroup()
                .addComponent(csLabel)
                .addComponent(this.csInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .addGap(10)
                .addComponent(acfLabel)
                .addComponent(this.acfInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                .addGap(10)
                .addComponent(fltTimeLabel)
                .addComponent(this.fltTimeInput, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE);

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        this.setLayout(layout);
    }

    private void onEnabledPropertyChange(PropertyChangeEvent event) {
        boolean enabled = (boolean) event.getNewValue();
        this.csInput.setEnabled(enabled);
        this.acfInput.setEnabled(enabled);
        this.fltTimeInput.setEnabled(enabled);
    }

    public String getCallsign() {
        return csInput.getText();
    }

    @Nullable
    public Aircraft getAircraft() {
        return aircraftRepo.find(acfInput.getText()).orElse(null);
    }

    @Nullable
    public Duration getFlightTime() {
        Matcher m = Pattern.compile("(?:([0-1]?[0-9]|2[0-3]):)?([0-5][0-9])")
                .matcher(fltTimeInput.getText());

        if (!m.matches()) return null;

        int hour = m.start(1) == -1 ? 0 : Integer.parseInt(m.group(1));
        int min = m.start(2) == -1 ? 0 : Integer.parseInt(m.group(2));
        return Duration.ofMinutes(hour * 60L + min);
    }

    public void setCallsign(String text) {
        csInput.setText(text);
    }

    public void setAircraft(String text) {
        acfInput.setText(text);
    }

    public void setFlightTime(String text) {
        fltTimeInput.setText(text);
    }

    public boolean validateForm() {
        boolean valid = true;

        if (csInput.getText().isBlank()) {
            csInput.setBorder(window.getAmberBorder());
            valid = false;
        } else {
            csInput.setBorder(window.getDefaultBorder(csInput));
        }

        if (getAircraft() == null) {
            acfInput.setBorder(window.getAmberBorder());
            valid = false;
        } else {
            acfInput.setBorder(window.getDefaultBorder(acfInput));
        }

        if (getFlightTime() == null) {
            fltTimeInput.setBorder(window.getAmberBorder());
            valid = false;
        } else {
            fltTimeInput.setBorder(window.getDefaultBorder(fltTimeInput));
        }

        return valid;
    }

    private DocumentListener getValidator() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateForm();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                validateForm();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                validateForm();
            }
        };
    }

}
