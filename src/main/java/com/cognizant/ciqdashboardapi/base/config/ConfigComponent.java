package com.cognizant.ciqdashboardapi.base.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Base64;

@Configuration
public class ConfigComponent {
    @Autowired
    Environment environment;

    @Bean("JasyptEncryptorBean")
    public StringEncryptor stringEncryptor(){
        String property = environment.getProperty("jasypt.encryptor.password");
        String decode = new String(Base64.getDecoder().decode(property));
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(decode);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setPoolSize(1);
        config.setIvGenerator(new RandomIvGenerator());
        config.setProviderName("SunJCE");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
