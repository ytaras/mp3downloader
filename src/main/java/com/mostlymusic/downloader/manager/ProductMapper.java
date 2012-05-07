package com.mostlymusic.downloader.manager;

import java.util.List;

import com.mostlymusic.downloader.client.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ytaras
 *         Date: 9/23/11
 *         Time: 5:21 PM
 */
public interface ProductMapper {
    String TABLE_NAME = "PRODUCTS";

    @Update("CREATE TABLE " + TABLE_NAME + " (productId INT NOT NULL PRIMARY KEY, " +
            "name VARCHAR(128) NOT NULL, description LONG VARCHAR NOT NULL, mainImage VARCHAR(256))")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(productId, name, description, mainImage) " +
            "VALUES (#{productId:NUMERIC}, #{name:VARCHAR}, #{description:VARCHAR}, #{mainImage:VARCHAR})")
    void insertProduct(Product product);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE productId = #{id}")
    Product loadProduct(long id);

    @Select("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE productId = #{id}")
    boolean productExists(long id);

    @Update("UPDATE " + TABLE_NAME + " SET name = #{name:VARCHAR}, description = #{description:VARCHAR}, " +
            "mainImage = #{mainImage:VARCHAR} WHERE productId = #{productId:NUMERIC}")
    void updateProduct(Product product);

    @Select("SELECT DISTINCT l.productId FROM " + TABLE_NAME + " p RIGHT OUTER JOIN " +
            ItemMapper.TABLE_NAME + " l ON l.productId = p.productId WHERE p.productId IS NULL AND l.productId != 0")
    List<Long> findUnknownProducts();
}
