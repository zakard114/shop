package com.heeju.shop.service;

import com.heeju.shop.entity.Member;
import com.heeju.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Transactional  // Declared in service layer class responsible for business logic.
                // When an error occurs while processing logic, the changed data
                // is called back to the state before logic execution.
@RequiredArgsConstructor // There are ways to inject a bean by declaring @Autowired
                         // or using field injection (Setter injection) or constructor
                         // injection. @RequiredArgsConstructor creates a constructor
                         // for fields marked final or @NonNull. If a bean has
                         // one constructor and the parameter type of the constructor
                         // can be registered as a bean, dependency injection is possible
                         // without the @Autowired annotation.
public class MemberService implements UserDetailsService {
    // MemberService implements "UserDetailsService".

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("Already a registered member.");
            // For members who are already registered, an IllegalStateException is thrown.
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        // Override the loadUserByUserName() method of the UserDetailsService interface.
        // Receive the email of the user to log in as a parameter.

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
        // Returns a User object that implements UserDetail. To create a User object,
        // pass the member's email, password, and role as parameters to the constructor.
    }
}
