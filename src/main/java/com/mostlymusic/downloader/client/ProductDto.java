package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:47 PM
 */
public class ProductDto {
    private int id;

    public ProductDto(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDto that = (ProductDto) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                '}';
    }
}
