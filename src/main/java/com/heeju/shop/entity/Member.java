package com.heeju.shop.entity;

import com.heeju.shop.constant.Role;
import com.heeju.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter @Setter
@Table(name = "member")
// Inherit BaseEntity class to apply Auditing to Member entity
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // Since members must be uniquely identified by email, specify the unique property
    // so that identical values cannot enter the database.
    @Column(unique = true)
    private String email;
    private String password;
    private String address;

    // Annotation for Java enum type. If you create and manage a method to
    // create members in the Member entity, you have the advantage of only having
    // to modify one place even if the code changes.
    @Enumerated(EnumType.STRING)
    private Role role;

    // Annotation for Java enum type. If you create and manage a method to create members
    // in the Member entity, you have the advantage of only having to modify one place
    // even if the code changes.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        // Encrypt password as BCryptPassword by passing the password field written
        // in memberFormDto as a parameter in PasswordEncoder passwordEncoder()
        // { return new BCryptPasswordEncoder() } registered as @Bean in SecurityConfig.
        // Due to stating @Bean, remote injection and utilization are possible.
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }

}
