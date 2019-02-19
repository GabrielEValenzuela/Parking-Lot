package com.concurrent.programming;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Log implements Observer {

    private static final Logger log = Logger.getLogger(" ");

    public Log() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String name = "./L" + dateFormat.format(date) + ".log";
            System.out.println(name);
            int limit = 500000000;
            Handler file = new FileHandler(name, limit, 1);
            file.setLevel(Level.ALL);
            file.setFormatter(new SimpleFormatter());
            log.addHandler(file);
            log.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logConfig(String msg) {
        log.log(Level.CONFIG, msg);
    }

    public static void logInfo(String msg) {
        log.log(Level.INFO, msg);
    }

    public static void logSevere(String msg) {
        log.log(Level.SEVERE, msg);
    }

    @Override
    public void update(String msg, int type) {
        switch (type) {
            case 1:
                logConfig(msg);
                break;
            case 2:
                logInfo(msg);
                break;
            case 3:
                logSevere(msg);
                break;
        }


    }
}
