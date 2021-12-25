/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KHUSHI-PC
 */
public class ProductDAO {
    
    public static String getNextProductId() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select max(p_id) from products");
        rs.next();
        String productid = rs.getString(1);
        if(productid == null)
            return "P101";
        int productno = Integer.parseInt(productid.substring(1));
        productno = productno+1;
        return "P"+productno;
    }
    
    public static boolean addProduct(ProductPojo p) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into products values(?,?,?,?,?,?,?,'Y')");
        ps.setString(1, p.getProductId());
        ps.setString(2, p.getProductName());
        ps.setString(3, p.getProductCompany());
        ps.setDouble(4, p.getProductPrice());
        ps.setDouble(5, p.getOurPrice());
        ps.setInt(6, p.getTax());
        ps.setInt(7, p.getQuantity());
        return ps.executeUpdate() == 1;
    }
    
    public static List<ProductPojo> getProductsDetails() throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from products where status='Y' order by p_id");
        ArrayList<ProductPojo> productsList = new ArrayList<>();
        while (rs.next())
        {
            ProductPojo p = new ProductPojo();
            p.setProductId(rs.getString(1));
            p.setProductName(rs.getString(2));
            p.setProductCompany(rs.getString(3));
            p.setProductPrice(rs.getDouble(4));
            p.setOurPrice(rs.getDouble(5));
            p.setTax(rs.getInt(6));
            p.setQuantity(rs.getInt(7));
            productsList.add(p);
        }             
        return productsList;
    }
    
    public static boolean deleteProduct(String productid) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products set status='N' where p_id=?");
        ps.setString(1, productid);
        return ps.executeUpdate() == 1;
    }
    
    public static boolean updateProduct(ProductPojo p) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products set p_name=?, p_companyname=?, p_price=?, our_price=?, p_tax=?, quantity=? where p_id=?");
        ps.setString(1, p.getProductName());
        ps.setString(2, p.getProductCompany());
        ps.setDouble(3, p.getProductPrice());
        ps.setDouble(4, p.getOurPrice());
        ps.setInt(5, p.getTax());
        ps.setInt(6, p.getQuantity());
        ps.setString(7, p.getProductId());
        return ps.executeUpdate() == 1;
    }
    
    public static ProductPojo getProductDetails(String id) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from products where p_id=? and status='Y'");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        ProductPojo p = new ProductPojo();
        while (rs.next())
        {
            p.setProductId(rs.getString(1));
            p.setProductName(rs.getString(2));
            p.setProductCompany(rs.getString(3));
            p.setProductPrice(rs.getDouble(4));
            p.setOurPrice(rs.getDouble(5));
            p.setTax(rs.getInt(6));
            p.setQuantity(rs.getInt(7));
        }             
        return p;
    }
    
    public static boolean updateStocks(List<ProductPojo> productsList) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products set quantity=quantity-? where p_id=?");
        int x=0;
        for(ProductPojo p:productsList)
        {
            ps.setInt(1, p.getQuantity());
            ps.setString(2, p.getProductId());
            int rows = ps.executeUpdate();
            if(rows!=0)
                x++;
        }
        return x==productsList.size();
    }
}
