package com.saiko.bidmarket.bidding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.saiko.bidmarket.bidding.entity.Bidding;
import com.saiko.bidmarket.product.entity.Product;

public interface BiddingRepository extends BiddingCustomRepository, JpaRepository<Bidding, Long> {
  List<Bidding> findAllByProductOrderByBiddingPriceDesc(Product product);

  void deleteAllBatchByBidderId(long bidderId);

  @Modifying
  @Query("delete from Bidding b where b.product.writer.id = :writerId and b.product.progressed = true")
  void deleteAllByWriterId(long writerId);
}
