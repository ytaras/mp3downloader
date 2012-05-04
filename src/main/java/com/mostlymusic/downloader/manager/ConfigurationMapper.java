package com.mostlymusic.downloader.manager;

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

    @Update("UPDATE " + TABLE_NAME + " SET threadCount = #{threadsNumber}")
    void setDownloadThreadsNumber(int threadsNumber);

    @Update("UPDATE " + TABLE_NAME + " SET autoDownload = #{autoDownload}")
    void setAutoDownload(boolean autoDownload);

    @Select("SELECT threadCount FROM " + TABLE_NAME)
    int getDownloadThreadsNumber();

    @Select("SELECT autoDownload FROM " + TABLE_NAME)
    boolean getAutoDownload();

    @Update("ALTER TABLE " + TABLE_NAME + " ADD COLUMN autoDownload SMALLINT DEFAULT 1")
    void toVersion2_1();

    @Update("ALTER TABLE " + TABLE_NAME + " ADD COLUMN threadCount SMALLINT DEFAULT 5")
    void toVersion2_2();

    @Update("ALTER TABLE " + TABLE_NAME + " ADD COLUMN frameSize char(100)")
    void toVersion3();

    @Select("SELECT frameSize FROM " + TABLE_NAME)
    FrameSize getFrameSize();

    @Update("UPDATE " + TABLE_NAME + " SET frameSize = #{frameSize} ")
    void setFrameSize(FrameSize frameSize);
}
