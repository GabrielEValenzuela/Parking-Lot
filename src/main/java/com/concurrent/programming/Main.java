package com.concurrent.programming;


import com.concurrent.programming.customexceptions.ConfigException;
import com.concurrent.programming.customexceptions.NoPlaceInvariants;
import com.concurrent.programming.loaders.LoadTransitions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// public class Main {
    /**
     * Class for test without using the UI interface. Uncomment for use it
     * @param args
     * @throws ConfigException
     * @throws NoPlaceInvariants
     * @throws InterruptedException
     */
   /* public static void main(String[] args) throws ConfigException, NoPlaceInvariants, InterruptedException {
        String configFile = "C:\\Users\\Valenzuela\\IdeaProjects\\ParkingLot\\src\\main\\java\\com\\concurrent\\programming\\variante1.json";
        AcquireData data = new AcquireData(configFile);
        LoadTransitions loadT = data.getData(LoadTransitions.class);

        Monitor monitor = new Monitor(configFile,policyType.RANDOM,policyType.STATICORDER);

        PoolFireManager poolfire = new PoolFireManager(loadT,monitor);

        ExecutorService executor = Executors.newFixedThreadPool(poolfire.init().size());
        for(int i = 0; i < poolfire.init().size(); i++){
            executor.execute(poolfire.init().get(i));
        }
        executor.shutdown();
        try {
            if(!executor.awaitTermination(10, TimeUnit.SECONDS)){
                executor.shutdownNow();
                System.exit(0);
            }
        }
        catch (InterruptedException e){
            ((ExecutorService) executor).shutdownNow();
            Thread.currentThread().interrupt();
            System.exit(0);
        }


    }
}
*/