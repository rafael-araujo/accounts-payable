package com.example.account.infrastructure.persistence;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSourceUrl, dataSourceUsername, dataSourcePassword)
                .locations("classpath:db/migration") // Defina o local dos seus scripts de migração
                .baselineOnMigrate(true) // Habilita o baseline automático na primeira migração (opcional)
                .cleanDisabled(false) // Desabilita a limpeza do banco de dados antes da migração (opcional)
                .outOfOrder(false) // Desabilita a execução de migrações fora de ordem (opcional)
                .load();
    }
}