package com.heeju.shop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberFormDto {

    @NotBlank(message = "Name is required.")
    private String name;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Please enter it in email format.")
    private String email;
    @NotEmpty(message = "Password is a required input.")
    @Length(min = 8, max = 16, message = "Please enter a password of at least 8 characters and no more than 16 characters.")
    private String password;

    @NotEmpty(message = "Address is a required input.")
    private String address;

}
