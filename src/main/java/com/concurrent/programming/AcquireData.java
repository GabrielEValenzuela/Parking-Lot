package com.concurrent.programming;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.FileSystemNotFoundException;

public class AcquireData {
    private final String dotJsonFile;
    private final File file;

    /**
     * Using the GSON Library, reads the information from a .json file and puts it
     * on the correspondig class. For more information see: <a href=https://sites.google.com/site/gson/Home>GSON</a>
     * @param dotJsonFile Path to file
     */
    public AcquireData(String dotJsonFile) {
        this.dotJsonFile = dotJsonFile;
        this.file = openFile(dotJsonFile);
    }

    private File openFile(String fileName) throws FileSystemNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileSystemNotFoundException("File " + this.dotJsonFile + " does not exist. Please verify the path/filename");
        }
        return file;
    }

    public <T> T getData(Type typeObject) {
        JsonReader reader;
        Gson gson;
        gson = new Gson();
        try {
            reader = new JsonReader(new FileReader(this.dotJsonFile));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return null;
        }
        return gson.fromJson(reader, typeObject);
    }

}
