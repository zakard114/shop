package com.heeju.shop.entity;

import com.heeju.shop.dto.MemberFormDto;
import com.heeju.shop.repository.CartRepository;
import com.heeju.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EntityManager em;

    // Create a method to create a member entity.
    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("test");
        memberFormDto.setAddress("po box 6969");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }
    
    @Test
    @DisplayName("Shopping Cart Member Entity Mapping Lookup Test")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // JPA stores data in the persistence context and then reflects it in the database by calling flush() at the end of the transaction. After storing the member entity and shopping cart entity in the persistence context, flush() is forcibly called from the entity manager to reflect them in the database.
        em.clear(); // JPA looks up entities from the persistence context and then searches the database if there are no entities in the persistence context. Let's empty the persistence context to see if the member entity is also retrieved when the shopping cart entity is retrieved from the actual database.

        Cart savedCart = cartRepository.findById(cart.getId()) // Retrieve saved shopping cart entities.
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId()); // Compare the ID of the member entity initially saved with the ID of the member entity mapped to savedCart.
        
    }

}