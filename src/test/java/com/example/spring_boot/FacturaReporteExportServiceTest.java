package com.example.spring_boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_boot.service.FacturaService;

@SpringBootTest
class FacturaReporteExportServiceTest {

	@Autowired
	private FacturaService facturaService;

	@Test
	void generaPdfYTxtEnLaRaizConElResultadoDeLaConsulta() throws Exception {
		facturaService.generarArchivosReporteEnRaiz();

		Path pdf = Path.of("reporte.pdf");
		Path txt = Path.of("reporte.txt");

		assertThat(Files.exists(pdf)).isTrue();
		assertThat(Files.size(pdf)).isGreaterThan(0L);
		assertThat(Files.exists(txt)).isTrue();
		assertThat(Files.size(txt)).isGreaterThan(0L);
		assertThat(Files.readString(txt)).contains("numeroFactura");
	}
}
