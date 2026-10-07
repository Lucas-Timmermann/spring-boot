package com.example.spring_boot.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.spring_boot.dto.FacturaReporteDTO;
import com.example.spring_boot.service.FacturaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaRestController {

	private final FacturaService facturaService;

	@GetMapping
	public List<FacturaReporteDTO> buscarFacturas(
			@RequestParam(value = "fechaDesde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
			@RequestParam(value = "fechaHasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
			@RequestParam(value = "estado", required = false) String estado,
			@RequestParam(value = "montoMinimo", required = false) Double montoMinimo) {
		if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"fechaDesde no puede ser posterior a fechaHasta");
		}
		if (montoMinimo != null && montoMinimo < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"montoMinimo no puede ser negativo");
		}
		return facturaService.buscarFacturas(fechaDesde, fechaHasta, estado, montoMinimo);
	}
}