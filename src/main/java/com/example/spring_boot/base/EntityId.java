package com.example.spring_boot.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//jpa
@MappedSuperclass

//lombok
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public abstract class EntityId {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}