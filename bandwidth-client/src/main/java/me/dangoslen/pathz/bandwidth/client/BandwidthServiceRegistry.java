package me.dangoslen.pathz.bandwidth.client;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingApiSpec;
import me.dangoslen.pathz.bandwidth.client.apis.messages.BandwidthMessagingService;
import me.dangoslen.pathz.bandwidth.client.apis.messages.HttpBandwidthMessagingService;
import org.apache.commons.lang3.StringUtils;

public class BandwidthServiceRegistry {

    private Feign.Builder builder;
    private String token;
    private String secret;
    private String basePath;

    public BandwidthServiceRegistry(String token, String secret, String basePath) {
        this.token = token;
        this.secret = secret;
        this.basePath = basePath;

        builder = Feign.builder()
                .contract(new JAXRSContract())
                .encoder(new JacksonEncoder())
                .logLevel(Logger.Level.BASIC);
    }

    public BandwidthMessagingService getBandwidthMessagingService() {
        return new HttpBandwidthMessagingService(buildApiClient(BandwidthMessagingApiSpec.class));
    }

    private <T> T buildApiClient(Class<T> clazz) {
        return buildApiClient(clazz, null);
    }

    private <T> T buildApiClient(Class<T> clazz, String additionalPath) {
        StringBuilder path = new StringBuilder(this.basePath);

        if (StringUtils.isNotBlank(additionalPath)) {
            path.append(additionalPath);
        }

        if (StringUtils.isNotBlank(token)) {
            builder.requestInterceptor(new BasicAuthRequestInterceptor(token, secret));
        }

        return builder
                .logger(new Slf4jLogger(clazz))
                .target(clazz, path.toString());
    }
}
