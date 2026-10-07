package com.example.spring_boot.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.spring_boot.base.EntityId;

//jpa
@Entity 
@Table (name = "domicilio")
//lombok
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString 
@EqualsAndHashCode (callSuper = true, onlyExplicitlyIncluded = true)

public class Domicilio extends EntityId {
    private String nombreCalle;
    private String numeroCalle;

}