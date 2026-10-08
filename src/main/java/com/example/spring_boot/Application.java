package com.example.spring_boot;

import java.io.IOException;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.spring_boot.service.FacturaService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ApplicationRunner generarReporteInicial(FacturaService facturaService) {
		return args -> {
			try {
				facturaService.generarArchivosReporteEnRaiz();
			} catch (IOException e) {
				throw new IllegalStateException("No se pudo generar el reporte raíz", e);
			}
		};
	}

}

