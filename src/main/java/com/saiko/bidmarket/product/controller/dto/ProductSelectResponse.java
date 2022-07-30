package com.saiko.bidmarket.product.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.saiko.bidmarket.product.entity.Image;
import com.saiko.bidmarket.product.entity.Product;

public class ProductSelectResponse {
  private final long id;
  private final String title;
  private final String thumbnailImage;
  private final int minimumPrice;
  private final LocalDateTime expireAt;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public ProductSelectResponse(long id, String title, String thumbnailImage, int minimumPrice,
                               LocalDateTime expireAt, LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.thumbnailImage = thumbnailImage;
    this.minimumPrice = minimumPrice;
    this.expireAt = expireAt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static ProductSelectResponse from(Product product) {
    return new ProductSelectResponse(product.getId(), product.getTitle(),
                                     getThumbnailUrl(product.getImages()),
                                     product.getMinimumPrice(), product.getExpireAt(),
                                     product.getCreatedAt(), product.getUpdatedAt());
  }

  private static String getThumbnailUrl(List<Image> images) {
    if (images != null && images.size() >= 1) {
      return images.get(0).getUrl();
    }
    return null;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getThumbnailImage() {
    return thumbnailImage;
  }

  public int getMinimumPrice() {
    return minimumPrice;
  }

  public LocalDateTime getExpireAt() {
    return expireAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }
}

