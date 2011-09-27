package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/23/11
 *         Time: 4:58 PM
 */
public class ArtistsMapperTest extends StoragetTestBase {

    private ArtistMapper artistMapper;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        injector.getInstance(SchemaCreator.class).createTables();
        artistMapper = injector.getInstance(ArtistMapper.class);
        Connection connection = injector.getInstance(DataSource.class).getConnection();
        try {
            connection.prepareStatement("DELETE FROM ARTISTS").execute();
            connection.prepareStatement("DELETE FROM LINKS").execute();
        } finally {
            connection.close();
        }
    }

    @Test
    public void shouldGetArtistById() throws Exception {
        // given
        Artist artist = new Artist();
        artist.setName("Name");
        artist.setArtistId(123);

        // when
        assertThat(artistMapper.artistExists(123)).isFalse();
        artistMapper.insertArtist(artist);
        assertThat(artistMapper.artistExists(123)).isTrue();
        Artist loaded = artistMapper.loadArtist(123);


        // then
        assertThat(loaded).isEqualTo(artist);
    }

    @Test
    public void shouldFindUnknownArtists() {
        // given
        ItemMapper itemMapper = injector.getInstance(ItemMapper.class);
        Item item = new Item();
        item.setMainArtistId(123);
        itemMapper.insertItem(item, new Account());
        Artist product = new Artist();
        product.setName("Name");
        product.setArtistId(123);

        // when
        List<Long> unknownProducts1 = artistMapper.findUnknownArtists();
        artistMapper.insertArtist(product);
        List<Long> unknownProducts2 = artistMapper.findUnknownArtists();
        // then
        assertThat(unknownProducts1).containsOnly(123L);
        assertThat(unknownProducts2).isEmpty();
    }
}