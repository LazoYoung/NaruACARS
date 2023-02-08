package com.naver.idealproduction.song;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppProperties {
    private static final Logger logger = Logger.getLogger(SimOverlayNG.class.getName());
    private static final String fileName = "properties.json";
    private static final AppProperties defaultProps = new AppProperties();
    private static File file = null;
    private int port = 8080;
    private String simbriefName = "";

    static {
        try {
            var path = SimOverlayNG.getDirectory().resolve(fileName);
            file = path.toFile();

            if (!file.isFile()) {
                var mapper = new ObjectMapper();
                mapper.writeValue(file, defaultProps);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to write " + fileName, e);
        }
    }

    public static AppProperties read() {
        try {
            var mapper = new ObjectMapper();
            return mapper.readValue(file, AppProperties.class);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to read " + fileName, e);
            return defaultProps;
        }
    }

    public boolean save() {
        try {
            var mapper = new ObjectMapper();
            mapper.writeValue(file, this);
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read " + fileName, e);
            return false;
        }
    }

    @JsonGetter("port")
    public int getPort() {
        return port;
    }

    @JsonGetter("simbrief-name")
    public String getSimbriefName() {
        return simbriefName;
    }

    @JsonSetter("port")
    public void setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port range!");
        }
        this.port = port;
    }

    @JsonGetter("simbrief-name")
    public void setSimbriefName(String simbriefName) {
        this.simbriefName = simbriefName;
    }
}