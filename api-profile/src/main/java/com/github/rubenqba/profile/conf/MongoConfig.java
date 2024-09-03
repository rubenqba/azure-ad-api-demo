package com.github.rubenqba.profile.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    @ConditionalOnResource(resources = "data.json")
    public Jackson2RepositoryPopulatorFactoryBean respositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("data.json")});
        return factory;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new JwtAuditorAware();
    }
}
