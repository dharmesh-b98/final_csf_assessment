package vttp.batch5.csf.assessment.server.controllers;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import vttp.batch5.csf.assessment.server.model.Order;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

  @Autowired RestaurantService restaurantService;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path="/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus() {
    JsonArray menuJsonArray = restaurantService.getMenu();
    return ResponseEntity.ok(menuJsonArray.toString());
  }


  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path="/food_order", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    Order order = Order.toOrder(payload);
    Boolean credentialsCorrect = restaurantService.checkCredentials(order.getUsername(), order.getPassword());
    if (!credentialsCorrect){
      return ResponseEntity.status(401).body(Json.createObjectBuilder().add("message", "Invalid username and/or password").build().toString());
    }
    else{
      String order_id = UUID.randomUUID().toString().substring(0,8);

      try{
        String response = restaurantService.makePayment(order_id, order, "Dharmesh");
        return ResponseEntity.ok().body(response);
      } catch (Exception e) {
        return ResponseEntity.status(500).body(Json.createObjectBuilder().add("message", e.getMessage()).build().toString());
      }
    }
  }
}
