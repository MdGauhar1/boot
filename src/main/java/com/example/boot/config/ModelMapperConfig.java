package com.example.boot.config;

import com.example.boot.entity.Review;
import com.example.boot.payload.ReviewDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Explicitly map the userId from the Review entity's User object
        modelMapper.addMappings(new PropertyMap<Review, ReviewDTO>() {
            @Override
            protected void configure() {
                map(source.getProduct().getId(), destination.getProductId());
                map(source.getUser().getId(), destination.getUserId());
            }
        });

        return modelMapper;
    }
}

