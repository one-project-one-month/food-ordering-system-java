package org._p1m.foodorderingsystem.config.beans;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Ignore the character for the request header
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedQueryChars", "[,],{,},|,\\,^,`,<,>");
            connector.setProperty("relaxedPathChars", "[,],{,},|,\\,^,`,<,>");
        });
    }
}
