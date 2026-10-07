package com.example.spring_boot.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_boot.dto.FacturaReporteDTO;
import com.example.spring_boot.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacturaService {

	private final FacturaRepository facturaRepository;

	@Transactional(readOnly = true)
	public List<FacturaReporteDTO> buscarFacturas(
			LocalDate fechaDesde,
			LocalDate fechaHasta,
			String estado,
			Double montoMinimo) {
		Date desde = fechaDesde == null ? null : aFecha(fechaDesde);
		Date hastaExclusiva = fechaHasta == null ? null : aFecha(fechaHasta.plusDays(1));
		String estadoNormalizado = estado == null || estado.isBlank() ? null : estado.trim();

		return facturaRepository.buscarReporte(desde, hastaExclusiva, estadoNormalizado, montoMinimo);
	}

	private Date aFecha(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay().toInstant(ZoneOffset.UTC));
	}
}