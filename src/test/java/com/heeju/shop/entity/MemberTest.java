package com.heeju.shop.entity;

import com.heeju.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing Test")
    @WithMockUser(username = "Heeju", roles = "USER") // With the annotation provided by Spring Security,
                                                      // you can proceed with testing assuming that the user specified
                                                      // in @WithMockUser is logged in.
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        // Audity area. The Member entity inherits the abstract class BaseEntity, which inherits Auditor.
        System.out.println("register time: "+member.getRegTime());
        System.out.println("update time: "+member.getUpdateTime());
        System.out.println("create member: "+member.getCreatedBy());
        System.out.println("modify member: "+member.getModifiedBy());

    }

}