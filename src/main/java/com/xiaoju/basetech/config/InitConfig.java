package com.xiaoju.basetech.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.xiaoju.basetech.util.Constants.LOG_PATH;

@Component
public class InitConfig implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        File f= new File(LOG_PATH);
        if(!f.exists()){
            f.mkdirs();
        }

    }
}
