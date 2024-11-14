/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant_management;

/**
 *
 * @author ADMIN
 */
public class product {
    private Integer id;
    private String productId;
    private String name;
    private String type;
    private Double price;
    private Integer quantity;
    
    public product(Integer id, String productId, String name, String type, Double price, Integer quantity){
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }
    
    public Integer getId(){
        return id;
    }
    public String getProductId(){
        return productId;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public Double getPrice(){
        return price;
    }
    public Integer getQuantity(){
        return quantity;
    }
}
