package com.heeju.shop.controller;

import com.heeju.shop.dto.MemberFormDto;
import com.heeju.shop.entity.Member;
import com.heeju.shop.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultHandlers;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc // Declare for MockMvc testing
@Transactional  // The annotation itself defines the scope of a single db transaction. DB transactions occur within the persistence context scope.
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc; // It is a dummy object that is similar to a real object using the MockMvc class,
                             // but has only the functions necessary for testing. Using the MockMvc object,
                             // you can test as if making a request from a web browser.

    // To proceed with the login example, create a method to register members before logging in.
    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("HEEJU");
        memberFormDto.setAddress("p.o.box 696969");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }
    
    @Test
    @DisplayName("Test login success")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);

        
        mockMvc.perform(formLogin()
                .userParameter("email") // Set email as ID using userParameter().
                                                        // Passed as .usernameParameter("email") to SecurityConfig's
                                                        // configure(). Then, pass it to Member member =
                                                        // memberRepository.findByEmail(email); of loadUserByUsername()
                                                        // of MemberService.
                .loginProcessingUrl("/members/login")   // After executing the membership registration method,
                                                        // test whether you can log in with the registered member information.
                                                        // Use userParameter() to set email as ID and request login URL.

                // From here, the elements required for tokenizing. Gather all the information below and tokenize http
                // AuthenticationManagerBuilder imported into SecurityConfig corresponds to the AuthenticationManager
                //  specified in the Spring Security Authentication Architecture table.
                .user(email)
                .password(password)) // Tokenized information to be compared to UserDetails of MemberService up to this point.
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
                // .andExpect is determined after all SecurityConfig processes are completed.
                // If the login is successful and authenticated, the test code passes.

    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin()
                .userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email)
                .password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // Membership registration proceeded normally,
        // but an attempt was made to log in with a different password than the password entered during membership registration,
        // and an unauthenticated result is output, and the test passes by .unauthenticated().

    }
}