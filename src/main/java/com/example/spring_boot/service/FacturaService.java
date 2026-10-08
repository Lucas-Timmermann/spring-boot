package com.example.spring_boot.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.spring_boot.dto.FacturaReporteDTO;
import com.example.spring_boot.repository.FacturaRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacturaService {

	private static final List<String> COLUMNAS = List.of(
			"numeroFactura",
			"fechaEmision",
			"clienteDenominacion",
			"condicionIva",
			"puntoVentaDescripcion",
			"importeTotal",
			"cantidadItems");

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

		if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"fechaDesde no puede ser posterior a fechaHasta");
		}
		if (montoMinimo != null && montoMinimo < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"montoMinimo no puede ser negativo");
		}

		return facturaRepository.buscarReporte(desde, hastaExclusiva, estadoNormalizado, montoMinimo);
	}

	@Transactional(readOnly = true)
	public void generarArchivosReporteEnRaiz() throws IOException {
		List<FacturaReporteDTO> facturas = buscarFacturas(null, null, null, null);
		Path raiz = Path.of(System.getProperty("user.dir"));
		generarPdf(raiz.resolve("reporte.pdf"), facturas);
		generarTxt(raiz.resolve("reporte.txt"), facturas);
	}

	private void generarPdf(Path destino, List<FacturaReporteDTO> facturas) throws IOException {
		try {
			Document document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(document, Files.newOutputStream(destino, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
			document.open();
			document.add(new Paragraph("Reporte de facturas"));
			document.add(new Paragraph(" "));

			PdfPTable table = new PdfPTable(COLUMNAS.size());
			table.setWidthPercentage(100f);
			table.setSpacingBefore(8f);
			table.setWidths(new float[] {1.2f, 1.6f, 3.2f, 2.3f, 2.5f, 1.8f, 1.2f});

			Font headerFont = new Font(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 9,
					Font.BOLD, BaseColor.WHITE);
			Font cellFont = new Font(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 8,
					Font.NORMAL, BaseColor.BLACK);

			for (String columna : COLUMNAS) {
				PdfPCell cell = new PdfPCell(new Phrase(columna, headerFont));
				cell.setBackgroundColor(BaseColor.DARK_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setPadding(6f);
				table.addCell(cell);
			}

			for (FacturaReporteDTO factura : facturas) {
				table.addCell(crearCelda(String.valueOf(factura.getNumeroFactura()), cellFont));
				table.addCell(crearCelda(formatearFecha(factura.getFechaEmision()), cellFont));
				table.addCell(crearCelda(factura.getClienteDenominacion(), cellFont));
				table.addCell(crearCelda(factura.getCondicionIva(), cellFont));
				table.addCell(crearCelda(factura.getPuntoVentaDescripcion(), cellFont));
				table.addCell(crearCelda(String.format("%.2f", factura.getImporteTotal()), cellFont));
				table.addCell(crearCelda(String.valueOf(factura.getCantidadItems()), cellFont));
			}

			document.add(table);
			document.close();
		} catch (DocumentException e) {
			throw new IOException("No se pudo generar el PDF del reporte", e);
		}
	}

	private void generarTxt(Path destino, List<FacturaReporteDTO> facturas) throws IOException {
		StringBuilder contenido = new StringBuilder();
		contenido.append(String.join("\t", COLUMNAS)).append(System.lineSeparator());
		for (FacturaReporteDTO factura : facturas) {
			contenido.append(String.join("\t",
					String.valueOf(factura.getNumeroFactura()),
					formatearFecha(factura.getFechaEmision()),
					normalizarTexto(factura.getClienteDenominacion()),
					normalizarTexto(factura.getCondicionIva()),
					normalizarTexto(factura.getPuntoVentaDescripcion()),
					String.format("%.2f", factura.getImporteTotal()),
					String.valueOf(factura.getCantidadItems())))
					.append(System.lineSeparator());
		}
		Files.writeString(destino, contenido.toString(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}

	private PdfPCell crearCelda(String valor, Font font) {
		PdfPCell celda = new PdfPCell(new Phrase(normalizarTexto(valor), font));
		celda.setPadding(5f);
		celda.setNoWrap(false);
		return celda;
	}

	private String normalizarTexto(String valor) {
		if (valor == null) {
			return "";
		}
		String texto = valor.trim();
		return texto.length() > 120 ? texto.substring(0, 120) + "..." : texto;
	}

	private String formatearFecha(LocalDate fecha) {
		return fecha == null ? "" : fecha.toString();
	}

	private Date aFecha(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay().toInstant(ZoneOffset.UTC));
	}
}