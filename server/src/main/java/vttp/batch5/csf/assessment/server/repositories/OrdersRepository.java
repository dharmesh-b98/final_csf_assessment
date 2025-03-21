package vttp.batch5.csf.assessment.server.repositories;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.model.Menu;
import vttp.batch5.csf.assessment.server.model.Order;


@Repository
public class OrdersRepository {

  @Autowired MongoTemplate mongoTemplate;
  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here

  /* 
  db.menus.find().sort({
    name:1
  }) 
  */
  public List<Menu> getMenu() {
    Criteria criteria = new Criteria();
    Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC,"name"));
    List<Document> docList = mongoTemplate.find(query,Document.class,"menus");
    List<Menu> menuList = new ArrayList<>();
    docList.stream().forEach((doc) -> {
      menuList.add(Menu.populateFromDocument(doc));
    });

    return menuList;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here


  /*
  /*
    db.orders.insert({
        "order_id":inputOrderId
        "username":rating
        .
        .
        .
    })
  */
  
  public Boolean insertOrders(String response, String username, BigDecimal total, Order order){
    JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
    String paymentId = responseJson.getString("payment_id");
    String orderId = responseJson.getString("order_id");
    Date orderDate = new Date(responseJson.getJsonNumber("timestamp").longValue());
  
    Document toInsert = new Document();

    toInsert.append("order_id", orderId)
            .append("payment_id", paymentId)
            .append("username", username)
            .append("total", total)
            .append("timestamp", orderDate)
            .append("items", order.getItems());
    
    mongoTemplate.insert(toInsert, "orders");
    return true;
  }
}
