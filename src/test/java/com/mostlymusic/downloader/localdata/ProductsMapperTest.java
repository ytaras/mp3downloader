package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.client.Product;
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
public class ProductsMapperTest extends StoragetTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        injector.getInstance(SchemaCreator.class).createTables();
        Connection connection = injector.getInstance(DataSource.class).getConnection();
        try {
            connection.prepareStatement("DELETE FROM PRODUCTS").execute();
            connection.prepareStatement("DELETE FROM LINKS").execute();
        } finally {
            connection.close();
        }
    }

    @Test
    public void shouldGetProductById() throws Exception {
        // given
        ProductMapper productMapper = injector.getInstance(ProductMapper.class);
        Product product = new Product(123);
        product.setDescription("Description");
        product.setMainImage("Main image");
        product.setName("Name");

        // when
        assertThat(productMapper.productExists(123)).isFalse();
        productMapper.insertProduct(product);
        assertThat(productMapper.productExists(123)).isTrue();
        Product loaded = productMapper.loadProduct(123);


        // then
        assertThat(loaded).isEqualTo(product);
    }

    @Test
    public void shoulUpdateProduct() throws Exception {
        // given
        ProductMapper productMapper = injector.getInstance(ProductMapper.class);
        Product product = new Product(123);
        product.setDescription("Description");
        product.setMainImage("Main image");
        product.setName("Name");

        // when
        productMapper.insertProduct(product);
        product.setDescription("Updated description");
        productMapper.updateProduct(product);
        Product loaded = productMapper.loadProduct(123);


        // then
        assertThat(loaded).isEqualTo(product);
    }

    @Test
    public void shouldFindUnknownProducts() {
        // given
        ItemMapper itemMapper = injector.getInstance(ItemMapper.class);
        ProductMapper productMapper = injector.getInstance(ProductMapper.class);
        Item item = new Item();
        item.setProductId(123);
        itemMapper.insertItem(item, new Account());
        Product product = new Product();
        product.setName("Name");
        product.setDescription("Description");
        product.setProductId(123);

        // when
        List<Long> unknownProducts1 = productMapper.findUnknownProducts();
        productMapper.insertProduct(product);
        List<Long> unknownProducts2 = productMapper.findUnknownProducts();
        // then
        assertThat(unknownProducts1).containsOnly(123L);
        assertThat(unknownProducts2).isEmpty();
    }
}
