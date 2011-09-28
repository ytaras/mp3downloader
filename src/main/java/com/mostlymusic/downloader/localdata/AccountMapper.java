package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.dto.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:47 PM
 */
public interface AccountMapper {

    String TABLE_NAME = "ACCOUNTS";

    @Update("CREATE TABLE " + TABLE_NAME + " (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "username VARCHAR(128) NOT NULL, lastOrderId BIGINT)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(username, lastOrderId) VALUES (#{account.username:VARCHAR}, #{account.lastOrderId:NUMERIC})")
    void createAccount(@Param("account") Account account);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id:NUMERIC}")
    void deleteAccount(int id);

    @Update("UPDATE " + TABLE_NAME + " SET username = #{username:VARCHAR}, lastOrderId = #{lastOrderId:NUMERIC} " +
            "WHERE id = #{id:NUMERIC}")
    void updateAccount(Account account);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE username = #{login}")
    Account findByLoginName(String login);

    @Select("SELECT username FROM " + TABLE_NAME + " WHERE username LIKE '${pattern}%' ORDER BY username")
    List<String> listLoginNames(@Param("pattern") String pattern);
}
