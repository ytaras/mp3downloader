package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 5:00 PM
 */
public interface ItemsMapper {
    String TABLE_NAME = "LINKS";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE accountId = #{id}")
    List<Item> listLinks(Account account);

    @Update("CREATE TABLE " + TABLE_NAME + " (itemId BIGINT NOT NULL PRIMARY KEY,\n" +
            "productId BIGINT ,\n" +
            "downloadsBought INT,\n" +
            "downloadsUsed INT,\n" +
            "linkId BIGINT,\n" +
            "linkTitle VARCHAR(255),\n" +
            "linkHash VARCHAR(255),\n" +
            "status VARCHAR(255),\n" +
            "fileName VARCHAR(255),\n" +
            "createdAt DATE,\n" +
            "updatedAt DATE,\n" +
            "accountId INT)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(linkId, linkTitle, itemId, productId, downloadsBought, linkHash, " +
            "downloadsUsed, status, fileName, createdAt, updatedAt, accountId) \n" +
            "VALUES (#{item.linkId:NUMERIC}, #{item.linkTitle:VARCHAR}, #{item.itemId:NUMERIC}, #{item.productId:NUMERIC}, " +
            "#{item.downloadsBought:NUMERIC}, #{item.linkHash:VARCHAR}, " +
            "#{item.downloadsUsed:NUMERIC}, #{item.status:VARCHAR}, #{item.fileName:VARCHAR}, #{item.createdAt:DATE}, " +
            "#{item.updatedAt:DATE}, #{account.id:NUMERIC})")
    void insertItem(@Param("item") Item item, @Param("account") Account account);
}
