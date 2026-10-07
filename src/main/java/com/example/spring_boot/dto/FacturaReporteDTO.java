package com.example.spring_boot.dto;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import lombok.Data;

@Data 
public class FacturaReporteDTO {
	private Long numeroFactura;
	private LocalDate fechaEmision;
	private String clienteDenominacion;
	private String condicionIva;
	private String puntoVentaDescripcion;
	private double importeTotal;
	private long cantidadItems;

	public FacturaReporteDTO(Long numeroFactura, Date fechaEmision, String clienteDenominacion,
			String condicionIva, String puntoVentaDescripcion, double importeTotal, long cantidadItems) {
		this.numeroFactura = numeroFactura;
		this.fechaEmision = fechaEmision == null ? null
				: fechaEmision.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
		this.clienteDenominacion = clienteDenominacion;
		this.condicionIva = condicionIva;
		this.puntoVentaDescripcion = puntoVentaDescripcion;
		this.importeTotal = importeTotal;
		this.cantidadItems = cantidadItems;
	}

}