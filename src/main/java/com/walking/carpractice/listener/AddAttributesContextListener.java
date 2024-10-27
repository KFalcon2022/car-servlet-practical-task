package com.walking.carpractice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.converter.db.CarConverter;
import com.walking.carpractice.converter.dto.CarDtoConverter;
import com.walking.carpractice.converter.dto.request.CreateCarRequestConverter;
import com.walking.carpractice.converter.dto.request.UpdateCarRequestConverter;
import com.walking.carpractice.repository.CarRepository;
import com.walking.carpractice.service.CarService;
import com.walking.carpractice.service.MigrationService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class AddAttributesContextListener implements ServletContextListener {
    private static final String HIKARI_PROPERTIES_PATH = "/WEB-INF/classes/hikari.properties";

    private static final Logger log = LogManager.getLogger(AddAttributesContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("Запущена инициализация атрибутов глобального контекста");

        var servletContext = event.getServletContext();

        var dataSource = hikariDataSource(servletContext);
        servletContext.setAttribute(ContextAttributeNames.DATASOURCE, dataSource);

        var carConverter = new CarConverter();
        servletContext.setAttribute(ContextAttributeNames.CAR_CONVERTER, carConverter);

        var carDtoConverter = new CarDtoConverter();
        servletContext.setAttribute(ContextAttributeNames.CAR_DTO_CONVERTER, carDtoConverter);

        var createCarRequestConverter = new CreateCarRequestConverter();
        servletContext.setAttribute(ContextAttributeNames.CREATE_CAR_REQUEST_CONVERTER, createCarRequestConverter);

        var updateCarRequestConverter = new UpdateCarRequestConverter();
        servletContext.setAttribute(ContextAttributeNames.UPDATE_CAR_REQUEST_CONVERTER, updateCarRequestConverter);

        var carRepository = new CarRepository(dataSource, carConverter);
        servletContext.setAttribute(ContextAttributeNames.CAR_REPOSITORY, carRepository);

        var carService = new CarService(carRepository);
        servletContext.setAttribute(ContextAttributeNames.CAR_SERVICE, carService);

        var migrationService = new MigrationService(dataSource);
        servletContext.setAttribute(ContextAttributeNames.MIGRATION_SERVICE, migrationService);

        var objectMapper = new ObjectMapper();
        servletContext.setAttribute(ContextAttributeNames.OBJECT_MAPPER, objectMapper);

        log.info("Завершена инициализация атрибутов глобального контекста");
    }

    private DataSource hikariDataSource(ServletContext servletContext) {
        try (var propertiesInputStream = servletContext.getResourceAsStream(HIKARI_PROPERTIES_PATH)) {
            var hikariProperties = new Properties();
            hikariProperties.load(propertiesInputStream);

            var configuration = new HikariConfig(hikariProperties);

            return new HikariDataSource(configuration);
        } catch (IOException e) {
            log.error("Невозможно загрузить конфигурацию для HikariCP", e);

            throw new RuntimeException(e);
        }
    }
}
