package vttp.batch5.csf.assessment.server.model;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class Order {
    private String username;
    private String password;
    private List<Item> items;

    public Order() {
        items = new ArrayList<>();
    }

    public Order(String username, String password, List<Item> items) {
        this.username = username;
        this.password = password;
        this.items = items;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    

    public static JsonObject toJson(Order order){
        return null;
    }

    public static Order toOrder(String orderJsonString){
        JsonObject orderJson = Json.createReader(new StringReader(orderJsonString)).readObject();
    
        String username = orderJson.getString("username");
        String password = orderJson.getString("password");
        List<Item> items = new ArrayList<>();

        JsonArray itemsJsonArray = orderJson.getJsonArray("items");
        for (int i=0; i<itemsJsonArray.size(); i++){
            JsonObject itemJson = itemsJsonArray.getJsonObject(i);
            Item item = Item.toItem(itemJson.toString());
            items.add(item);
        }

        Order order = new Order(username, password, items);
        return order;
    }

    
}
