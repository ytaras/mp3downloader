package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.dto.Account;
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

    @Select("SELECT * FROM " + TABLE_NAME + " ORDER BY id")
    List<Account> listAccounts();

    @Update("CREATE TABLE " + TABLE_NAME + " (id INT NOT NULL PRIMARY KEY, " +
            "username VARCHAR(128) NOT NULL, password VARCHAR(123) NOT NULL)")
    void createTable();

}
