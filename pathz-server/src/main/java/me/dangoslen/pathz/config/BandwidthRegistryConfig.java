package me.dangoslen.pathz.config;

import me.dangoslen.pathz.bandwidth.client.BandwidthServiceRegistry;
import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BandwidthRegistryConfig {

    @Value("${bandwidth.api.token}")
    private String token;

    @Value("${bandwidth.api.secret}")
    private String secret;

    @Value("${bandwidth.api.basePath")
    private String basePath;

    @Bean
    public BandwidthServiceRegistry getBandwidthServiceRegistry() {
        return new BandwidthServiceRegistry(token, secret, basePath);
    }

    @Bean
    @Autowired
    public BandwidthMessagingService bandwidthMessagingService(BandwidthServiceRegistry serviceRegistry) {
        return serviceRegistry.getBandwidthMessagingService();
    }

}
