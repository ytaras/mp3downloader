package com.mostlymusic.downloader.manager;

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
interface ItemMapper {
    String TABLE_NAME = "LINKS";

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE accountId = #{id}")
    List<Item> listItems(Account account);

    @Update("CREATE TABLE " + TABLE_NAME + " (itemId BIGINT NOT NULL PRIMARY KEY,\n" +
            "productId BIGINT ,\n" +
            "parentProductId BIGINT ,\n" +
            "mainArtistId BIGINT,\n" +
            "productName VARCHAR(255),\n" +
            "downloadsBought INT,\n" +
            "downloadsUsed INT,\n" +
            "linkId BIGINT,\n" +
            "linkTitle VARCHAR(255),\n" +
            "linkHash VARCHAR(255),\n" +
            "status VARCHAR(255),\n" +
            "fileName VARCHAR(255),\n" +
            "createdAt TIMESTAMP,\n" +
            "updatedAt TIMESTAMP,\n" +
            "accountId INT,\n" +
            "dirty SMALLINT)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(linkId, linkTitle, itemId, productId, downloadsBought, linkHash, " +
            "parentProductId, mainArtistId, productName, downloadsUsed, status, fileName, createdAt, updatedAt, accountId, dirty) \n" +
            "VALUES (#{item.linkId:NUMERIC}, #{item.linkTitle:VARCHAR}, #{item.itemId:NUMERIC}, #{item.productId:NUMERIC}, " +
            "#{item.downloadsBought:NUMERIC}, #{item.linkHash:VARCHAR}, " +
            "#{item.parentProductId:NUMERIC}, #{item.mainArtistId:VARCHAR}, #{item.productName:VARCHAR}, " +
            "#{item.downloadsUsed:NUMERIC}, #{item.status:VARCHAR}, #{item.fileName:VARCHAR}, #{item.createdAt:DATE}, " +
            "#{item.updatedAt:DATE}, #{account.id:NUMERIC}, #{item.dirty:BOOLEAN})")
    void insertItem(@Param("item") Item item, @Param("account") Account account);


    @Update("UPDATE " + TABLE_NAME + " SET linkTitle = #{item.linkTitle}, productId = #{item.productId}, " +
            "downloadsBought = #{item.downloadsBought}, linkId = #{item.linkId}," +
            "linkHash = #{item.linkHash}, parentProductId = #{item.parentProductId}, mainArtistId = #{item.mainArtistId}," +
            "productName = #{item.productName}, downloadsUsed = #{item.downloadsUsed}, status = #{item.status}," +
            "fileName = #{item.fileName}, createdAt = #{item.createdAt}, dirty = #{item.dirty}, accountId = #{account.id}")
    void updateItem(@Param("item") Item item, @Param("account") Account account);

    @Select("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE itemId = #{itemId}")
    boolean contains(long itemId);

    @Update("UPDATE " + TABLE_NAME + " SET status = #{status} WHERE itemId = #{itemId}")
    void setStatus(@Param("itemId") long itemId, @Param("status") String status);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE status = #{status} AND accountId = #{account.id}")
    List<Item> findItemsByStatus(@Param("account") Account account, @Param("status") String status);
}
