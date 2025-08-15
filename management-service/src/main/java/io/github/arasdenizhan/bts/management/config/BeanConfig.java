package io.github.arasdenizhan.bts.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class BeanConfig {

    @Bean
    ObjectMapper objectMapper(){
        return JsonMapper.builder()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndAddModules().build();
    }

    @Bean
    public Clock clock(){
        return Clock.system(ZoneId.of("Europe/Istanbul"));
    }
}
