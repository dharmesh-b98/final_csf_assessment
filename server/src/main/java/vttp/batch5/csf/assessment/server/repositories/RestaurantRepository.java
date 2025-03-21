package vttp.batch5.csf.assessment.server.repositories;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired JdbcTemplate jdbcTemplate;


    //check if username exists
    public Boolean checkCredentials(String username, String password){
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SqlQueries.checkCredentials_SQL, username, password);
        rs.next();
        Integer count = rs.getInt("count");
        return (count>0);
    }

    public Boolean insertPlaceOrder(String response, BigDecimal total, String username ){
        JsonObject responseJson = Json.createReader(new StringReader(response)).readObject();
        String paymentId = responseJson.getString("payment_id");
        String orderId = responseJson.getString("order_id");
        Date orderDate = new Date(responseJson.getJsonNumber("timestamp").longValue());

        int rowsUpdated = jdbcTemplate.update(SqlQueries.insertPlaceOrders_SQL,orderId, paymentId, orderDate, total, username);
        if (rowsUpdated > 0){
            return true;
        }
        return false;
    }

}
