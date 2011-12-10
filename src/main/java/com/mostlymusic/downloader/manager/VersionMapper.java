package com.mostlymusic.downloader.manager;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ytaras
 *         Date: 11/2/11
 *         Time: 11:26 AM
 */
public interface VersionMapper {
    String TABLE_NAME = "DOWNLOADER_VERSION";

    @Update("CREATE TABLE " + TABLE_NAME + " (version INT NOT NULL)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(version) VALUES (1)")
    void createInitialConfig();

    @Update("UPDATE " + TABLE_NAME + " SET version = #{version}")
    void setVersion(int version);

    @Select("SELECT version FROM " + TABLE_NAME)
    int loadVersion();
}
