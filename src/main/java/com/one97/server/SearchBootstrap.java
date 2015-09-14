package com.one97.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.common.base.Strings;
import com.one97.common.conf.ConfigurationSourceProvider;
import com.one97.common.conf.FileConfigurationSourceProvider;
import com.one97.common.service.AbstractLifecycleService;
import com.one97.kernel.KernelServiceImpl;
import com.one97.settings.SettingsBuilder;
import com.one97.settings.SpockSettings;
import com.one97.settings.SpockSettingsBuilder;
import org.elasticsearch.monitor.jvm.JvmInfo;
import org.elasticsearch.monitor.process.JmxProcessProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/*
 * _____________________________________________________________________________________________
 *
 * 		Copyright (c)  2015. Prashant Kumar - All Rights Reserved.
 * 			-	Unauthorized copying of this file, via any medium is strictly prohibited.
 * 			-	This file is Proprietary and Confidential.
 *
 * _____________________________________________________________________________________________
 */
public class SearchBootstrap {

    private static volatile Thread keepAliveThread;
    private static volatile CountDownLatch keepAliveLatch;
    private static Logger LOGGER = LoggerFactory.getLogger(SearchServer.class);
    private final String[] arguments;

    public SearchBootstrap(String... args) {
        this.arguments = args;
    }

    public void start() {

        setupLogging();

        String confPath = System.getProperty("spock.conf.path");

        if (Strings.isNullOrEmpty(confPath)) {
            confPath = System.getProperty("user.dir");
            LOGGER.warn("spock.conf.path is not found in system property. Using location {}", confPath);
        }

        File logbackFile = Paths.get(confPath, "logback.xml").toFile();
        File spockConfFile = Paths.get(confPath, "spock-searcher.conf.yml").toFile();
        
        if (logbackFile.exists()) {
            resolveConfig(logbackFile);
        } else {
            LOGGER.warn("Logger configuration not found, hoping, your bad day will teach you! tolerating and continuing .. ");
        }

        ConfigurationSourceProvider configuration = null;

        if (spockConfFile.exists()) {
            configuration = new FileConfigurationSourceProvider(spockConfFile.toString());
        } else {
            if (Strings.isNullOrEmpty(confPath)) {
                LOGGER.error("File {} does not exists in root path or you need to pass -Dspock.conf.path=<conf-directory-name> to specify a location", spockConfFile.toString());
            } else {
                LOGGER.error("File {} does not exists in specified location {}", spockConfFile.toString(), confPath);
            }
            LOGGER.error("Exiting ..");
            System.exit(-1);
        }

        SettingsBuilder settingsBuilder = new SpockSettingsBuilder(configuration);
        SpockSettings settings = settingsBuilder.build();

        AbstractLifecycleService service = new
                KernelServiceImpl(settings);

        try {

            //TODO: When not started properly need to decide whether the service would even complete the process
            //      of starting or we will terminate.
            //      The Lifecycle STATE can be tested for that -- in fact partial indexes (organizations) can be
            //      started as well.
            service.start();
        } catch (Exception e) {
            service.stop();
            LOGGER.error("error during initialization phase, refer exception details. exiting..", e);
            System.exit(-1);
        }

        keepAliveLatch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {
            @Override
            public void run() {
                LOGGER.info("Shutting down ...");
                keepAliveLatch.countDown();
            }
        });

        keepAliveThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    keepAliveLatch.await();
                } catch (InterruptedException e) {
                    // bail out
                }
            }
        }, "search-server");

        keepAliveThread.setDaemon(false);
        keepAliveThread.start();

        LOGGER.info("max_open_files {}", JmxProcessProbe.getMaxFileDescriptorCount());

        if (JvmInfo.jvmInfo().vmName().toLowerCase(Locale.ROOT).contains("client")) {
            LOGGER.warn("jvm uses the client vm, make sure to run `java` with the server vm " +
                    "for best performance by adding `-server` to the command line");
        }

        LOGGER.info("Search server started its business ..");

    }

    private void resolveConfig(File loggingFile) {

        LoggerContext context = null;

        try {

            context = (LoggerContext) LoggerFactory.getILoggerFactory();

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            context.reset();
            configurator.doConfigure(loggingFile);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Logger initialized using {}", loggingFile.getAbsolutePath());
            }

        } catch (JoranException je) {
            //
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }

    private void setupLogging() {
        try {
            getClass().getClassLoader().loadClass("ch.qos.logback.classic.Logger");
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            // no logback
        } catch (Exception e) {
            System.err.println("Failed to configure logging...");
            e.printStackTrace();
        }
    }

}
