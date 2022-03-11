package me.longbow122.Homeroom.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Class written to read from the JSON configuration file that 'Homeroom' makes use of.
 */
public class ConfigReader {

    private final File config;

    /**
     * Constructor used to get an instance of the class to access methods as and when needed.
     * No need to pass an instance of the File object every time, so passing it once through the constructor will do the trick.
     * @param config {@link File} object representing the configuration file of 'Homeroom'.
     */
    public ConfigReader(File config) {
        this.config = config;
    }

    /**
     * Gets the connection string from the main configuration file of 'Homeroom'.
     * Handles a {@link FileNotFoundException} internally if it fails to find the configuration file.
     * @return {@link String} representing the connection string with placeholders. Placeholders should be replaced by a valid username and password when being used to log into a database.
     */
    public String getConnectionStringFromConfig() {
        JsonElement configTree;
        try {
            configTree = JsonParser.parseReader(new FileReader(config)).getAsJsonObject().get("configuration");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (configTree == null || !configTree.isJsonArray()) return null;
        JsonArray tree = configTree.getAsJsonArray();
        for (JsonElement x : tree) {
            if (!(x.isJsonObject())) continue;
            JsonObject potentialString = x.getAsJsonObject();
            if (!(potentialString.has("connectionString"))) continue;
            return potentialString.get("connectionString").toString();
        }
        return null;
    }
}
