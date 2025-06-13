package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min=3, message = "Street name must be at least 3 characters")
    private String street;
    @NotBlank
    @Size(min=3, message = "Building name must be at least 3 characters")
    private String building;
    @NotBlank
    @Size(min=3, message = "City name must be at least 3 characters")
    private String city;
    @NotBlank
    @Size(min=2, message = "State name must be at least 2 characters")
    private String state;
    @NotBlank
    @Size(min=3, message = "Country name must be at least 3 characters")
    private String country;
    @NotBlank
    @Size(min=5, message = "Zipcode name must be at least 5 characters")
    private String zipCode;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Address(String street, String building, String city, String state, String country, String zipCode) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }
}
