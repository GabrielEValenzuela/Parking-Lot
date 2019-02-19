package com.controller;

import com.concurrent.programming.*;
import com.concurrent.programming.customexceptions.ConfigException;
import com.concurrent.programming.customexceptions.NoPlaceInvariants;
import com.concurrent.programming.loaders.LoadTransitions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Principal {
    @FXML
    private Button btnOpen;
    @FXML
    private Button btnInit;

    private String configFile;

    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void fileChooser(ActionEvent eventChoose) {
        FileChooser fileChooser = new FileChooser();


        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");

        fileChooser.getExtensionFilters().add(extensionFilter);

        File select = fileChooser.showOpenDialog(null);

        if (select != null) {
            System.out.println(select.getAbsolutePath());
            this.configFile = select.getAbsolutePath();
        }
    }

    public void init(ActionEvent eventInit) throws ConfigException, NoPlaceInvariants {

        AcquireData data = new AcquireData(configFile);
        LoadTransitions loadT = data.getData(LoadTransitions.class);

        Monitor monitor = new Monitor(configFile, policyType.RANDOM, policyType.STATICORDER);

        PoolFireManager poolfire = new PoolFireManager(loadT, monitor);

        ExecutorService executor = Executors.newFixedThreadPool(poolfire.init().size());
        for (int i = 0; i < poolfire.init().size(); i++) {
            executor.execute(poolfire.init().get(i));
        }
        executor.shutdown();
        alert.setTitle("Operación finalizada con éxito");
        alert.setHeaderText(null);
        alert.setContentText("Por detalles revisar el archivo .log creado");

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                alert.showAndWait();
                executor.shutdownNow();
                Log.logInfo("Executor size: " + poolfire.init().size());
                System.exit(0);
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.exit(0);
        }
    }
}
