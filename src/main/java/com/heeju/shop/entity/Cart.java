package com.heeju.shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart extends BaseEntity{
    
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // If a default value is defined in the table or auto increment
                                                    // is defined in the table, use Auto.
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY) // Use the @OneToOne annotation to map member entities one-to-one.
                                      // When the number of mapped entities is large, it is difficult for developers to
                                      // predict how queries will be executed in eager mode, and performance problems
                                      // occur because unused data is searched all at once. This is the need for LAZY loading mode.
    @JoinColumn(name = "member_id") // Specify the foreign key to map using the @JoinColumn annotation. In the name
                                    // property, set the name of the foreign key to be mapped. If you do not specify
                                    // the name of @JoinColumn, JPA automatically finds the ID, but the column name may
                                    // not be generated as desired, so specify it directly.
    private Member member;
}
