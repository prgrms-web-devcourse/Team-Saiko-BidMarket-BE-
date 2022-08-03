package com.saiko.bidmarket.bidding.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saiko.bidmarket.bidding.entity.Bidding;
import com.saiko.bidmarket.product.entity.Product;

public interface BiddingRepository extends JpaRepository<Bidding, Long> {
  List<Bidding> findAllByProductOrderByBiddingPriceDesc(Product product);
}
