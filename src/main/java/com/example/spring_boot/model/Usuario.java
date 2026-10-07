package com.example.spring_boot.model;


import com.example.spring_boot.base.EntityId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//jpa
@Entity 
@Table(name = "usuario")
//lombok
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString 
@EqualsAndHashCode (callSuper = true, onlyExplicitlyIncluded = true)

public class Usuario extends EntityId {

    @Column(nullable = false)
    private String usuario;
    @Column(nullable = false)
    private String clave;
    @Column(nullable = false)  
    private String nombre; 
    @Column(nullable = false)
    private String apellido;
}