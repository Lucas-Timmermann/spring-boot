package com.example.spring_boot.model;

import com.example.spring_boot.base.AuditoriaApp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//jpa
@Entity
@Table(name = "cliente")
//lombok
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@ToString 
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)

public class Cliente extends AuditoriaApp {

    @Column (nullable = false)
    private String cuitCuil;
    @Column (nullable = false)
    private String denominacion;
    @OneToOne 
    @JoinColumn (name = "contacto_id", nullable = false)
    private Contacto contacto;
    @OneToOne
    @JoinColumn (name = "domicilio_id", nullable = false)
    private Domicilio domicilio;

}