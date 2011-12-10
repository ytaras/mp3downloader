package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.client.Artist;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 4:48 PM
 */
public interface ArtistMapper {
    String TABLE_NAME = "ARTISTS";

    @Update("CREATE TABLE " + TABLE_NAME + " (artistId INT NOT NULL PRIMARY KEY, name VARCHAR(128) NOT NULL)")
    void createSchema();

    @Select("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE artistId = #{id}")
    boolean artistExists(long id);

    @Insert("INSERT INTO " + TABLE_NAME + " (artistId, name) VALUES (#{artistId:NUMERIC}, #{name:VARCHAR})")
    void insertArtist(Artist artist);

    @Select("SELECT * FROM " + TABLE_NAME + " WHERE artistId = #{id}")
    Artist loadArtist(long id);

    @Select("SELECT DISTINCT l.mainArtistId FROM " + TABLE_NAME + " a RIGHT OUTER JOIN " +
            ItemMapper.TABLE_NAME + " l ON l.mainArtistId = a.artistId WHERE a.artistId IS NULL AND l.mainArtistId <> 0")
    List<Long> findUnknownArtists();

    @Select("SELECT * FROM " + TABLE_NAME)
    List<Artist> listArtists();
}
