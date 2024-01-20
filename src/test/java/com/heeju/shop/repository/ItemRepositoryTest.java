package com.heeju.shop.repository;

import com.heeju.shop.constant.ItemSellStatus;
import com.heeju.shop.entity.Item;
import com.heeju.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Test code description")
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em; // Inject EntityManager bean using @PersistenceContext
                      // to use persistence context.
                      // concurrency problems do not occur when EntityManager
                      // is injected with @PersistenceContext.

    @Autowired
    ItemRepository itemRepository;

    public void createItemList(){
        for (int i = 0; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("Test Product"+i);
            item.setPrice(10000+i);
            item.setItemDetail("Test product detail"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("Product storage testing")
    public void createItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("Test Product1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("itemNm, itemDetail or test")
    void findByItemOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail("Test Product1", "Test product detail5");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Price LessThan Test")
    void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Test descending price lookup")
    void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }

    @Test
    void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemDetail("Test product detail");
        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("Product search test using nativeQuery property")
    void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("Test product detail");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl query test 1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // Dynamically create queries using
        // JPAQueryFactory. Enter an EntityManager object as a constructor parameter.
        QItem qItem = QItem.item; // To create a query through Querydsl, use the QItem object automatically
        // created through the plugin.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem) // The source can be written similarly to SQL statements using qItem
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "Test product detail" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch(); // Query results are returned as a list using fetch, one of the JPAQuery methods.
        // The query statement is executed when the fetch() method is executed.

        // Methods that return results in JPAQuery are as follows -
        // List<T> fetch(): Returns a list of search results
        // T fetchOne: If there is only one search target, returns the type specified as a generic
        // T fetchFirst(): Returns only one item among the search targets
        // Long fetchCount(): Returns the number of search targets
        // QueryResult<T> fetchResults(): Returns QueryResults including the searched list and total number.

    }

    public void createItemList2 () {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("Test Product" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("Test product detail" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("Test Product" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("Test product detail" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("Querydsl query test 2")
    public void queryDslTest2() {
        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // BooleanBuilder is a builder that creates conditions for queries.
        // It implements Predicate and can be used in a method chain format.
        QItem item = QItem.item;
        String itemDetail = "Test product detail";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        // The “and” condition needed to search for the required product is being added.
        // In the source below, you can see that the sales status condition is dynamically added
        // to booleanBuilder only when the product's sales status is SELL.
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        // Create a Pageable object using the PageRequest.of() method to page and view data.
        // The first argument is the number of the page to be searched, and the second argument
        // is the number of data to be searched per page.
        Page<Item> itemPagingResult =
                itemRepository.findAll(booleanBuilder, pageable);
        // Data that meets the conditions is received as a Page object using the findAll() method
        // defined in the QueryDslPredicateExecutor interface.
        System.out.println("total elements: "+
                itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }

}