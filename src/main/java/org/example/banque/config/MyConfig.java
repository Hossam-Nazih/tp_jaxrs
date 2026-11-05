package org.example.banque.config;

import org.example.banque.web.ClientRestJaxRSAPI;
import org.example.banque.web.CompteRestJaxRSAPI;
import org.example.banque.web.ErreurHttpMapper;
import org.example.banque.web.OperationRestJaxRSAPI;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Déclare à Jersey les classes qui exposent l'API REST. */
@Configuration
public class MyConfig {

    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig configurationJersey = new ResourceConfig();
        configurationJersey.register(CompteRestJaxRSAPI.class);
        configurationJersey.register(ClientRestJaxRSAPI.class);
        configurationJersey.register(OperationRestJaxRSAPI.class);
        configurationJersey.register(ErreurHttpMapper.class);
        // Sans cette option, une erreur 400 est redirigée vers /error, que Jersey transforme en 404
        configurationJersey.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        return configurationJersey;
    }
}
