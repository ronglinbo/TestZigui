/*
* 文 件 名:OrderResult.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-13
* 版 本 号： 1.10
*/
package com.wcyc.zigui2.bean;
import java.io.Serializable;

/**
 * 续费项目实体类
 * 
 * @author 姜韵雯
 * @version 1.10
 * @since 1.10
 */
public class ProductResult implements Serializable {
	private static final long serialVersionUID = -6323247127336102866L;
	private String productID;
    private String name;
    private Float price;
    private String unit;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
