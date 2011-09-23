package com.mostlymusic.downloader.client;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:47 PM
 */
public class ProductDto {
    private int productId;
    private String name;
    private String description;
    private List<Media> media;


    public ProductDto(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDto that = (ProductDto) o;

        if (productId != that.productId) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (media != null ? !media.equals(that.media) : that.media != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (media != null ? media.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", media=" + media +
                '}';
    }

    public static class Media {
        private String url;
        private String label;
        private int position;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Media media = (Media) o;

            if (position != media.position) return false;
            if (label != null ? !label.equals(media.label) : media.label != null) return false;
            if (url != null ? !url.equals(media.url) : media.url != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = url != null ? url.hashCode() : 0;
            result = 31 * result + (label != null ? label.hashCode() : 0);
            result = 31 * result + position;
            return result;
        }

        @Override
        public String toString() {
            return "Media{" +
                    "url='" + url + '\'' +
                    ", label='" + label + '\'' +
                    ", position=" + position +
                    '}';
        }
    }
}
