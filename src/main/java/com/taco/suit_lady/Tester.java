package com.taco.suit_lady;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.taco.suit_lady.util.tools.ResourceTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Tester {
    
    public static void main(String[] args) {
        runJsonTest();
    }
    
    private static void runJsonTest() {
        try {
            // create a writer
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/json/customer.json"));
        
            // create customer object
            JsonObject customer = new JsonObject();
            customer.put("id", 1);
            customer.put("name", "John Doe");
            customer.put("email", "john.doe@example.com");
            customer.put("age", 32);
        
            // create address object
            JsonObject address = new JsonObject();
            address.put("street", "155 Middleville Road");
            address.put("city", "New York");
            address.put("state", "New York");
            address.put("zipCode", 10045);
        
            // add address to customer
            customer.put("address", address);
        
            // add customer payment methods
            JsonArray pm = new JsonArray();
            pm.addAll(Arrays.asList("PayPal", "Stripe"));
            customer.put("paymentMethods", pm);
        
            // create projects
            JsonArray projects = new JsonArray();
        
            // create 1st project
            JsonObject p1 = new JsonObject();
            p1.put("title", "Business Website");
            p1.put("budget", 4500);
        
            // create 2nd project
            JsonObject p2 = new JsonObject();
            p2.put("title", "Sales Dashboard");
            p2.put("budget", 8500);
        
            // add projects
            projects.addAll(Arrays.asList(p1, p2));
        
            // add projects to customer
            customer.put("projects", projects);
        
            // write JSON to file
            Jsoner.serialize(customer, writer);
        
            //close the writer
            writer.close();
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
