package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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

    @Update("CREATE TABLE " + TABLE_NAME + " (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "username VARCHAR(128) NOT NULL, lastOrderId BIGINT, lastLoggedIn SMALLINT, password VARCHAR(128))")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(username, lastOrderId, password) VALUES (#{account.username:VARCHAR}, #{account.lastOrderId:NUMERIC}, " +
            "#{account.password:VARCHAR})")
    void createAccount(@Param("account") Account account);

    @Delete("DELETE FROM " + TABLE_NAME + " WHERE id = #{id:NUMERIC}")
    void deleteAccount(int id);

    @Update("UPDATE " + TABLE_NAME + " SET username = #{username:VARCHAR}, lastOrderId = #{lastOrderId:NUMERIC}, " +
            "password = #{password:VARCHAR} " +
            "WHERE id = #{id:NUMERIC}")
    void updateAccount(Account account);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE username = #{login:VARCHAR}")
    Account findByLoginName(String login);

    @Select("SELECT username FROM " + TABLE_NAME + " WHERE username LIKE '${pattern}%' ORDER BY lastLoggedIn, username")
    List<String> listLoginNames(@Param("pattern") String pattern);

    @Update("UPDATE " + TABLE_NAME + " SET lastLoggedIn = CASE WHEN username = #{loginName} THEN 1 ELSE 0 END")
    void setLastLoggedIn(String loginName);
}
