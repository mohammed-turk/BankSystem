package com.example.projectthree.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String position;

    @Column(columnDefinition = "double not null check (salary >= 0)")
    private Double salary;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;
}
