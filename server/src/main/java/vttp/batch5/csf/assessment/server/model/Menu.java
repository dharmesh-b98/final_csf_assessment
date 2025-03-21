package vttp.batch5.csf.assessment.server.model;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Menu {
    private String id;
    private String name;
    private String description;
    private Double price;
    public Menu() {
    }
    public Menu(String id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public static Menu populateFromDocument(Document doc){
        String id = doc.getString("_id");
        String name = doc.getString("name");
        String description = doc.getString("description");
        Double price = doc.getDouble("price");
        return new Menu(id,name,description,price);
    }

    public static JsonObject getJson(Menu menu){
        JsonObject menuJson = Json.createObjectBuilder().add("id", menu.getId())
                                                        .add("name", menu.getName())
                                                        .add("description", menu.getDescription())
                                                        .add("price", menu.getPrice())
                                                        .build();
        return menuJson;

    }
}
