package com.mostlymusic.downloader.localdata;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 4:13 PM
 */
public interface ConfigurationMapper {
    String TABLE_NAME = "CONFIGURATION";

    @Update("CREATE TABLE " + TABLE_NAME + " (savePath VARCHAR(255), refreshRate BIGINT)")
    void createSchema();

    @Insert("INSERT INTO " + TABLE_NAME + "(savePath, refreshRate) VALUES (#{defaultDownloadPath}, 5)")
    void insertConfig(String defaultDownloadPath);

    @Select("SELECT savePath FROM " + TABLE_NAME + " FETCH FIRST ROW ONLY")
    String getDownloadPath();

    @Update("UPDATE " + TABLE_NAME + " SET savePath = #{path}")
    void setDownloadPath(String path);

    @Select("SELECT refreshRate FROM " + TABLE_NAME + " FETCH FIRST ROW ONLY")
    int getRefreshRate();

    @Select("UPDATE " + TABLE_NAME + " SET refreshRate = #{rate}")
    void setRefreshRate(long rate);

    void setDownloadThreadsNumber(int threadsNumber);

    void setAutoDownload(boolean autoDownload);
}
