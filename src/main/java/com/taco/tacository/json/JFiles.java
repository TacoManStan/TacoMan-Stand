package com.taco.tacository.json;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.mongodb.client.MongoClients;
import com.taco.suit_lady.util.tools.Exc;
import org.bson.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class JFiles {
    private JFiles() { } //No Instance
    
    //<editor-fold desc="--- SAVE ---">
    
    public static void save(@NotNull JObject root) {
        System.out.println("Saving " + root.getJID() + "...");
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(pathPrefix + root.getJID() + ".json"));
            Jsoner.serialize(root.getJValue(), writer);
            writer.close();
        } catch (IOException e) {
            throw Exc.ex(e);
        }
    }
    
    public static void saveMongoDB(String databaseName, String collectionName, JObject obj) {
        MongoClients.create(mongoDbConnectionString)
                    .getDatabase(databaseName)
                    .getCollection(collectionName)
                    .insertOne(Document.parse(JFiles.getJson(obj)));
    }
    
    public static <T extends JLoadable> @NotNull List<T> loadMongoDB(String databaseName, String collectionName, Supplier<T> factory) {
        ArrayList<T> elements = new ArrayList<>();
        MongoClients.create(mongoDbConnectionString)
                    .getDatabase(databaseName)
                    .getCollection(collectionName)
                    .find(Document.class)
                    .forEach(document -> elements.add(loadFromString(factory.get(), document.toJson())));
        return elements;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- LOAD ---">
    
    public static <T extends JLoadable> @NotNull T loadFromString(@NotNull T jLoadable, String json) {
        return load(jLoadable, new StringReader(json));
    }
    
    @Contract("_ -> param1")
    public static <T extends JLoadable> @NotNull T load(@NotNull T jLoadable) {
        try {
            return load(jLoadable, Files.newBufferedReader(Paths.get(pathPrefix + jLoadable.getJID() + ".json")));
        } catch (IOException e) {
            throw Exc.ex(e);
        }
    }
    
    public static <T extends JLoadableObject> @NotNull T load(String jID, @NotNull T jLoadableObject) {
        jLoadableObject.setJID(jID);
        return load(jLoadableObject);
    }
    
    
    private static <T extends JLoadable> @NotNull T load(@NotNull T jLoadable, @NotNull Reader reader) {
        try {
            jLoadable.load((JsonObject) Jsoner.deserialize(reader));
            return jLoadable;
        } catch (JsonException e) {
            throw Exc.ex(e);
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- JSON ---">
    
    public static String getJsonFromFile(String jID) {
        try {
            return java.nio.file.Files.readString(getPath(jID));
        } catch (IOException e) {
            throw Exc.ex(e);
        }
    }
    
    public static String getJson(@NotNull JObject jObject) {
        try {
            JsonObject jsonObject = jObject.getJValue();
            StringWriter writer = new StringWriter();
            Jsoner.serialize(jsonObject, writer);
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            throw Exc.ex(e);
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="--- HELPER METHODS ---">
    
    private static final String pathPrefix = "src/main/resources/json/";
    private static final String mongoDbConnectionString = "mongodb://localhost:27017";
    
    private static @NotNull Path getPath(@NotNull String jID) {
        return Paths.get(pathPrefix + jID + ".json");
    }
    
    //</editor-fold>
}
