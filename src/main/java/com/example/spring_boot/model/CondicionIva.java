package com.example.spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.spring_boot.base.AuditoriaApp;

//jpa
@Entity
@Table(name = "condicion_iva")
//lombok
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString 
@EqualsAndHashCode (callSuper = true, onlyExplicitlyIncluded = true)

public class CondicionIva extends AuditoriaApp {
    @Column (nullable = false)
    private int codigoAfip;
    @Column (nullable = false)
    private String denominacion;
    
}