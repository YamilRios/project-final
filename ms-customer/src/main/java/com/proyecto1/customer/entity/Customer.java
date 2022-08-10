package com.proyecto1.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "schema_people.customer")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    private String id;
    
    @NotEmpty
    private String name;
    
    @NotEmpty
    private String lastName;
    
    @NotEmpty
    private String docNumber;
    
    @NotNull
    private int typeCustomer;
    
    @NotEmpty
    private String descTypeCustomer;
}
