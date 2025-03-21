package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp.batch5.csf.assessment.server.model.Item;
import vttp.batch5.csf.assessment.server.model.Menu;
import vttp.batch5.csf.assessment.server.model.Order;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired RestaurantRepository restaurantRepository;
  @Autowired OrdersRepository ordersRepository;
  RestTemplate restTemplate = new RestTemplate();

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    JsonArrayBuilder job = Json.createArrayBuilder();
    List<Menu> menuList = ordersRepository.getMenu();
    menuList.stream().forEach(
      (menu) -> {
        job.add(Menu.getJson(menu));
      }
    );

    JsonArray menuJsonArray = job.build();
    return menuJsonArray;
  }
  
  // TODO: Task 4

  public Boolean checkCredentials(String username, String password){
    return restaurantRepository.checkCredentials(username, password);
  }

  public BigDecimal getTotalPrice(Order order){
    List<Item> items = order.getItems();
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (Item item : items){
      totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
    }

    totalPrice = totalPrice.setScale(2, RoundingMode.CEILING);
    return totalPrice;   
  }


  @Transactional
  public String makePayment(String order_id, Order order, String payee){
    String url = "https://payment-service-production-a75a.up.railway.app/api/payment";

    HttpHeaders customHeader = new HttpHeaders();
    customHeader.set("X-Authenticate", order.getUsername());

    RequestEntity<String> request = RequestEntity.post(url)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .headers(customHeader)
                                                    .body(getPaymentJson(order_id, order.getUsername(), payee, getTotalPrice(order)).toString());

    ResponseEntity<String> response = restTemplate.exchange(request, String.class);

    Boolean sqlsuccess = restaurantRepository.insertPlaceOrder(response.getBody(), getTotalPrice(order), order.getUsername());

    Boolean mongosuccess = ordersRepository.insertOrders(response.getBody(), order.getUsername(), getTotalPrice(order), order);

    JsonObject responseJson = Json.createReader(new StringReader(response.getBody())).readObject();
        String paymentId = responseJson.getString("payment_id");
        String orderId = responseJson.getString("order_id");
        Long timeStamp = responseJson.getJsonNumber("timestamp").longValue();

    return Json.createObjectBuilder().add("orderId", orderId)
                                      .add("paymentId", paymentId)
                                      .add("total", getTotalPrice(order))
                                      .add("timestamp", timeStamp)
                                      .build()
                                      .toString();
  }


  public JsonObject getPaymentJson(String order_id, String payer, String payee, BigDecimal payment){
    JsonObject paymentJson = Json.createObjectBuilder().add("order_id", order_id)
                                                        .add("payer", payer)
                                                        .add("payee",payee)
                                                        .add("payment",payment)
                                                        .build();
    return paymentJson;
  }

}
