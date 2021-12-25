/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author KHUSHI-PC
 */
public class OrderDAO {
    
    public static String getNextOrderId() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select max(order_id) from orders");
        rs.next();
        String ordId = rs.getString(1);
        if(ordId == null)
            return "O-101";
        int ordno = Integer.parseInt(ordId.substring(2));
        ordno=ordno+1;
        return "O-"+ordno;
    }
    
    public static boolean addOrder(ArrayList<ProductPojo> al, String ordId) throws SQLException
    {        
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into orders values(?,?,?,?)");
        int count =0;
        for(ProductPojo p:al)
        {
            ps.setString(1, ordId);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserid());
            count=count+ps.executeUpdate();
        }
        return count==al.size();
    }
    
    public static List<String> getAllOrderId() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select order_id from orders order by order_id");
        ArrayList<String> allId = new ArrayList<>();
        while(rs.next())
        {
            allId.add(rs.getString(1));
        }
        return allId;
    }
    
    
    public static Map<String,String> getQuantity(String ordId) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select p_id,quantity from orders where order_id=?");
        ps.setString(1, ordId);
        HashMap <String,String> quantityList=new HashMap<>();
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            String pid=rs.getString(1);
            String quantity=rs.getString(2);
            quantityList.put(pid,quantity);
        }
        return quantityList;
    }
    
    
    public static List<ProductPojo> getOrderDetails(String ordId) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select p_id from orders where order_id=?");
        ps.setString(1, ordId);
        ResultSet rs = ps.executeQuery();
        List<ProductPojo> orderDetails = new ArrayList<>();
        while(rs.next())
        {
            ProductPojo p = new ProductPojo();
            Object [] row = new Object[8];
            String pid = rs.getString(1);
            p=ProductDAO.getProductDetails(pid);
            orderDetails.add(p);
        }
        return orderDetails;
    }
    
    public static List<String> getReceptionistOrderId (String userid) throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select order_id from orders where userid=?");
        ps.setString(1, userid);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> orderId = new ArrayList<>();
        while(rs.next())
        {
            orderId.add(rs.getString(1));
        }
        return orderId;
    }
}
