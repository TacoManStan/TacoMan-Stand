package com.taco;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.taco.tacository.json.JFiles;
import com.taco.tacository.json.TestData;
import com.taco.tacository.json.TestSubData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Tester {
    
    public static void main(String[] args) {
//        runJsonTest2();
        runJsonTest3();
    }
    
    private static void runMongoDBTest1() {
    }
    
    private static void runJsonTest3() {
        TestData testData = new TestData();
        JFiles.load("HAHAHAHAHAHAHA fart.", testData);
        testData.print();
    }
    
    private static void runJsonTest2() {
        TestSubData subData = new TestSubData("var3", "heheXD", "i am var2", 22);
        TestSubData[] subDataArray = new TestSubData[]{
                new TestSubData("I. Am. RAAAAAAAAALPH.", "54345", "ffffff", 999996),
                new TestSubData("I am NOT Ralph.", "heheXOOOO", "asdf", 32),
                new TestSubData("I am... maybe Ralph?", "asdfasd", "eeee", 9999),
                new TestSubData("I am DEFINITIELY Ralph. Maybe.", "asdfasdfasdfkasd", "fjfjf", 333252)
        };
        
        
        final String[] s1 = new String[]{"Matrix 0,0", "Matrix 1, 0", "Matrix 2, 0"};
        final String[] s2 = new String[]{"Matrix 0,1", "Matrix 1, 1", "Matrix 2, 1"};
        final String[] s3 = new String[]{"Matrix 0,2", "Matrix 1, 2", "Matrix 2, 2"};
        
        final String[][] sm = new String[][]{s1, s2, s3};
        
        
        TestData testData = new TestData("HAHAHAHAHAHAHA fart.", "Eggplant", 69, subData, subDataArray, sm, "Array Element 1", "Array Element 2", "Egggggs", "Walrus", "Random String", "1972 Western Chicago Suburb District High School Championship");
        
        JFiles.save(testData);
    }
    
    private static void runJsonTest1() {
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
