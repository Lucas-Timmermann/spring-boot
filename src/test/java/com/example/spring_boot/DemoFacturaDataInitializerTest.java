package com.example.spring_boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_boot.dto.FacturaReporteDTO;
import com.example.spring_boot.repository.FacturaRepository;

@SpringBootTest
class DemoFacturaDataInitializerTest {

	@Autowired
	private FacturaRepository facturaRepository;

	@Test
	void persisteCuatroFacturasYLaConsultaLasIncluyeEnElReporte() {
		List<FacturaReporteDTO> facturas = facturaRepository.buscarReporte(null, null, null, null);

		assertThat(facturas).hasSize(4);
		assertThat(facturas).extracting(FacturaReporteDTO::getNumeroFactura)
				.containsExactly(1001L, 1002L, 1003L, 1004L);
		assertThat(facturas).extracting(FacturaReporteDTO::getCantidadItems)
				.containsExactly(2L, 5L, 1L, 3L);
		assertThat(facturas).extracting(FacturaReporteDTO::getClienteDenominacion)
				.containsExactly("Ana Lopez", "Empresa Tech S.A.", "Carla Gomez", "Diego Sistemas S.R.L.");
		assertThat(facturas).extracting(FacturaReporteDTO::getPuntoVentaDescripcion)
				.containsExactly("Sucursal Central", "Sucursal Norte", "Sucursal Sur", "Sucursal Oeste");
	}
}
