package org.sanlam.controllers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    private String awsRegion = "eu-west-1";

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder().region(Region.of(awsRegion)).build();
    }
}
