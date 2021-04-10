package com.beraat.gateway.filter;



import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RequestFilterFactory implements GatewayFilterFactory<RequestFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) ->
                chain.filter(exchange).then(Mono.fromRunnable(() -> {
                })));
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }


    public static class Config {
    }
}