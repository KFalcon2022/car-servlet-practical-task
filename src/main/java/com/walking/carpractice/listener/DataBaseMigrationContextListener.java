package com.walking.carpractice.listener;

import com.walking.carpractice.constant.ContextAttributeNames;
import com.walking.carpractice.service.MigrationService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseMigrationContextListener implements ServletContextListener {
    private static final Logger log = LogManager.getLogger(DataBaseMigrationContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("Запущено применение миграций");

        var servletContext = event.getServletContext();

        var migrationService = (MigrationService) servletContext.getAttribute(ContextAttributeNames.MIGRATION_SERVICE);

        migrationService.migrate();

        log.info("Завершено применение миграций");
    }
}
