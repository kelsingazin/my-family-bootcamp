package com.family.myfamily.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeans {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
