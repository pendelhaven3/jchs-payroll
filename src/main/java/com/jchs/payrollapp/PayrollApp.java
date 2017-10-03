package com.jchs.payrollapp;

import java.awt.SplashScreen;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jchs.payrollapp.controller.StageController;
import com.jchs.payrollapp.gui.component.ShowDialog;

import javafx.application.Application;
import javafx.stage.Stage;

@SpringBootApplication
public class PayrollApp extends Application {

    private static String[] args;
    private static SplashScreen splashScreen;
    
    public static void main(String[] args) throws Exception {
        PayrollApp.args = args;
        splashScreen = SplashScreen.getSplashScreen();
        launch(args);
    }

    private ApplicationContext context;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        context = SpringApplication.run(PayrollApp.class, args);

        if (isDatabaseNotFound()) {
            ShowDialog.error("Database not found");
        } else {
            closeSplashScreen();
            showMainMenuScreen();
        }
    }

    private void closeSplashScreen() {
        if (splashScreen != null) {
            splashScreen.close();
        }
    }

    private boolean isDatabaseNotFound() {
        DataSource dataSource = context.getBean(DataSource.class);

        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            return true;
        }

        return false;
    }

    private void showMainMenuScreen() {
        StageController stageController = context.getBean(StageController.class);
        stageController.setStage(stage);
        stageController.showMainMenuScreen();
        stage.setResizable(true);
        stage.show();
    }

}