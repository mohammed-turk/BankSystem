package com.example.projectthree.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "accountNumber cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$", message = "accountNumber must follow the format XXXX-XXXX-XXXX-XXXX")
    @Column(columnDefinition = "varchar(19) not null")
    private String accountNumber;

    @NotNull(message = "balance cannot be null")
    @PositiveOrZero(message = "balance must be a non-negative number")
    @Column(columnDefinition = "double not null")
    private Double balance;

    @Column(columnDefinition = "boolean not null default false")
    private Boolean isActive = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn
    private Customer customer;
}
