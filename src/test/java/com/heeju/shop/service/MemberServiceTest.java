package com.heeju.shop.service;

import com.heeju.shop.dto.MemberFormDto;
import com.heeju.shop.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // If @Transactional is declared in the test class,
               // rollback is processed after test execution.
               // This allows you to test the same method repeatedly.
@TestPropertySource(locations = "classpath:application-test.properties")
// Specify application-test.properties created only for testing so that
// application.properties is not automatically referenced.
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;
    // public PasswordEncoder registered as @Bean in SecurityConfig
    // passwordEncoder(){return new BCryptPasswordEncoder();}

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("unknown");
        memberFormDto.setName("unknown location");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("Membership Test")
    void saveMemberTest() {
        // Use the assertEquals method of Junit's Assertion class to compare
        // the value requested to be saved in this test with the actual saved data.
        // Enter the expected value in the first parameter and the actually
        // stored value in the second parameter.
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    // Test code to check whether an error message is displayed properly when attempting
    // to sign up with a duplicate email.
    @Test
    @DisplayName("Duplicate membership test")
    public void saveDuplicateMemberTest() {
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member1);
            // You can test exception handling by using the assertThrows method
            // of Junit's Assertions class. Enter the exception type to occur
            // in the first parameter. If a duplicate is determined from
            // memberService.saveMember(Member member), the message
            // “Already a registered member.” is thrown. For example, if you enter member1
            // already registered above instead of member2 here.
        });

        assertEquals("Already a registered member.", e.getMessage());
        // Verify that the exception message that occurred matches the expected result.
        // When attempting to sign up with a duplicate email, the error message
        // "Already a registered member." thrown from MemberService's saveMember(Member member)
        // method is normally displayed.

    }
}