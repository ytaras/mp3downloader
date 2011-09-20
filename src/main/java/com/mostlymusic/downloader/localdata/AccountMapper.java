package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.dto.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:47 PM
 */
public interface AccountMapper {

    String TABLE_NAME = "ACCOUNTS";

    @Select("SELECT * FROM " + TABLE_NAME + " ORDER BY username")
    List<Account> listAccounts();

    @Update("CREATE TABLE " + TABLE_NAME + " (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "username VARCHAR(128) NOT NULL, password VARCHAR(123) NOT NULL, lastOrderId BIGINT)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(username, password, lastOrderId) VALUES (#{username}, #{password}, #{lastOrderId:NUMERIC})")
    void createAccount(Account account);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id}")
    void deleteAccount(int id);

    @Update("UPDATE " + TABLE_NAME + " SET username = #{username}, password = #{password}, " +
            "lastOrderId = #{lastOrderId:NUMERIC} WHERE id = #{id}")
    void updateAccount(Account account);
}
