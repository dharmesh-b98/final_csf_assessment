package vttp.batch5.csf.assessment.server.repositories;

public class SqlQueries {
    public static final String checkUserExists_SQL = "select count(username) as count from customers where username=?";

    public static final String checkCredentials_SQL = """
            select count(username) as count from customers where 
            username=? and
            password = sha2(?,224)
            """;
    
    public static final String insertPlaceOrders_SQL = """
            insert into place_orders(order_id, payment_id, order_date, total, username)
	        values(?,?,?,?,?)
            """;
}
