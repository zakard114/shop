package com.heeju.shop.repository;

import com.heeju.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item> {
    // "Predicate" is a function that provides the basis for determining that
    // ‘this condition is correct.’ In order to pass a Predicate to the Repository
    // as a parameter, it inherits the "QueryDslPredicateExecutor" interface.

    // The QueryDslPredicateExecutor interface declares the following methods.

    // long count(Predicate): Returns the total number of data that meets the condition
    // boolean exists(Predicate): Returns whether data that meets the condition exists or not
    // Iterate findAll(Predicate, Pageable): Returns all data that meets the condition
    // Page<T> findAll(Predicate, Pageable): Return page data that meets the conditions
    // Iterable findAll(Predicate, Sort): Returns sorted data that meets the conditions
    // T findOne(Predicate): Returns 1 data that meets the condition

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value = "select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);



}
