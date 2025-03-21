package vttp.batch5.csf.assessment.server.model;

import java.io.StringReader;
import java.math.BigDecimal;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Item {
    private String id;
    private BigDecimal price;
    private Integer quantity;
    public Item() {
    }
    public Item(String id, BigDecimal price, Integer quantity) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public static JsonObject toJson(Item item){
        return null;
    }

    public static Item toItem(String itemJsonString){
        JsonObject itemJson = Json.createReader(new StringReader(itemJsonString)).readObject();
        String id = itemJson.getString("id");
        BigDecimal price =  itemJson.getJsonNumber("price").bigDecimalValue();
        Integer quantity = itemJson.getInt("quantity");
        Item item = new Item(id, price, quantity);
        return item;
    }

    

}
