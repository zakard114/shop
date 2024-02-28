package com.heeju.shop.entity;

import com.heeju.shop.constant.ItemSellStatus;
import com.heeju.shop.repository.ItemRepository;
import com.heeju.shop.repository.MemberRepository;
import com.heeju.shop.repository.OrderItemRepository;
import com.heeju.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("Test Product");
        item.setPrice(10000);
        item.setItemDetail("Product Specification");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("Persistence transition test")
    public void cascadeTest(){

        Order order = new Order();

        for (int i=0; i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); // Contains the orderItem entity that has not yet been stored in the persistence context into the order entity.
        }

        orderRepository.saveAndFlush(order); // Forcefully call flush while saving the order entity to reflect the objects in the persistence context to the database.
        em.clear(); // Initialize the state of the persistence context.

        Order savedOrder = orderRepository.findById(order.getId()) // Due to initialize the persistence context and retrieve the order entity from the database. It is confirmed in the console window that the select query statement is executed.
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(3, savedOrder.getOrderItems().size()); // Check whether the three itemOrder entities are actually stored in the database.

    }

    // Orphan object removal test
    @Autowired
    MemberRepository memberRepository;

    // Create a method to create and save order data.
    public Order createOrder(){
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item); // item 영속화로 order엔티티에서 List<OrderItem> orderItems를 통해 가용할 수 있게 하는 듯
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }


    @Test
    @DisplayName("Orphan object removal test")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0); // Remove the 0th index element of the orderItem list managed by the order entity.
        em.flush();
    }
    
    // Lazy loading test
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("Lazy loading test")
    public void lazyLoadingTest(){
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush(); // JPA searches the entity from the persistence context and then searches the DB if there is
                    // no entity in the persistence context. After storing entities in the persistence context,
                    // flush() is forcibly called from the entity manager to reflect them in the DB.
        em.clear(); // Clear the persistence context for clear testing. Since the order entity has already been secured
                    // before flushing to the DB above, it is safe to empty the persistence context.
        
       OrderItem orderItem = orderItemRepository.findById(orderItemId)  // After initializing the state of the persistence context,
               // reinitialize the orderItem in the database using the order product ID stored in the order entity.
               // If you look up orderItem data in the code, you can see an incredibly long query statement
               // in the console window. Although we only searched for one orderItem entity, we are importing them all at
               // once by joining the order_item table and the item, orders, and member tables.
               .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class: "+orderItem.getOrder().getClass()); // Prints the class of the order object in
        // the orderItem entity. You can see that the Order class is displayed. - Output result: Order class: class com.shop.entity.Order
        // After modifying the mapping strategy in OrderItem.java to FethchType.LAZY, the Order class search result is output as HibernateProxy.
        // When set to Lazy Loading, proxy objects are placed instead of actual entities.
        System.out.println("===============================");
        orderItem.getOrder().getOrderDate();                                 // The proxy object does not load data until it is actually used,
        // and the search query statement is executed at the time of actual use. You can see that the select query statement is executed when
        // searching the orderDate of Order in the orderItem.getOrder().getClass() code above. You can easily understand it by running it in
        // debugging mode and executing the code line by line.
                                                                             // Execute query when checking order date
        System.out.println("===============================");
    }

}