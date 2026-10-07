package com.example.spring_boot.config;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.spring_boot.base.AuditoriaApp;
import com.example.spring_boot.model.Articulo;
import com.example.spring_boot.model.Cliente;
import com.example.spring_boot.model.CondicionIva;
import com.example.spring_boot.model.Contacto;
import com.example.spring_boot.model.Domicilio;
import com.example.spring_boot.model.FacturaVenta;
import com.example.spring_boot.model.FacturaVentaDetalle;
import com.example.spring_boot.model.ListaPrecio;
import com.example.spring_boot.model.ListaPrecioArticulo;
import com.example.spring_boot.model.Marca;
import com.example.spring_boot.model.PuntoVenta;
import com.example.spring_boot.model.Rubro;
import com.example.spring_boot.model.TipoMoneda;
import com.example.spring_boot.model.Usuario;
import com.example.spring_boot.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.EntityManager;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class DemoFacturaDataInitializer implements ApplicationRunner {

	private final EntityManager entityManager;
	private final FacturaRepository facturaRepository;
	private final TransactionTemplate transactionTemplate;

	@Override
	public void run(ApplicationArguments args) {
		if (facturaRepository.count() != 0) {
			return;
		}
		transactionTemplate.executeWithoutResult(status -> crearDatosDemo());
	}

	private void crearDatosDemo() {
		Usuario usuario = new Usuario();
		usuario.setUsuario("demo");
		usuario.setClave("demo");
		usuario.setNombre("Usuario");
		usuario.setApellido("Demo");
		entityManager.persist(usuario);
		entityManager.flush();

		CondicionIva condicionIva = new CondicionIva();
		condicionIva.setCodigoAfip(1);
		condicionIva.setDenominacion("IVA Responsable Inscripto");
		prepararAuditoria(condicionIva, usuario, LocalDate.of(2026, 1, 1));
		entityManager.persist(condicionIva);

		TipoMoneda moneda = new TipoMoneda();
		moneda.setCodigoAfip("PES");
		moneda.setDenominacion("Peso argentino");
		moneda.setSimbolo("$");
		prepararAuditoria(moneda, usuario, LocalDate.of(2026, 1, 1));
		entityManager.persist(moneda);

		Rubro rubro = new Rubro();
		rubro.setDenominacion("Informatica");
		rubro.setCodigo(10);
		prepararAuditoria(rubro, usuario, LocalDate.of(2026, 1, 1));
		entityManager.persist(rubro);

		Marca marca = new Marca();
		marca.setDenominacion("Marca Demo");
		marca.setCodigo(20);
		prepararAuditoria(marca, usuario, LocalDate.of(2026, 1, 1));
		entityManager.persist(marca);

		ListaPrecio listaPrecio = new ListaPrecio();
		listaPrecio.setCodigo("LP-DEMO");
		listaPrecio.setDenominacion("Lista de precios demo");
		prepararAuditoria(listaPrecio, usuario, LocalDate.of(2026, 1, 1));
		entityManager.persist(listaPrecio);

		List<FacturaVenta> facturas = List.of(
				crearFactura(1001L, LocalDate.of(2026, 3, 15), "20111111111", "Ana Lopez",
						"ana@example.test", "San Martin", 101, "Sucursal Central", 1,
						1500.0, "EMITIDA", 2, "ART-001", "Teclado", 750.0,
						condicionIva, moneda, rubro, marca, listaPrecio, usuario),
				crearFactura(1002L, LocalDate.of(2026, 3, 20), "20222222222", "Empresa Tech S.A.",
						"compras@tech.example.test", "Belgrano", 202, "Sucursal Norte", 2,
						85000.5, "EMITIDA", 5, "ART-002", "Notebook", 17000.1,
						condicionIva, moneda, rubro, marca, listaPrecio, usuario),
				crearFactura(1003L, LocalDate.of(2026, 3, 22), "27333333339", "Carla Gomez",
						"carla@example.test", "Rivadavia", 303, "Sucursal Sur", 3,
						4250.0, "ANULADA", 1, "ART-003", "Mouse", 4250.0,
						condicionIva, moneda, rubro, marca, listaPrecio, usuario),
				crearFactura(1004L, LocalDate.of(2026, 3, 25), "30444444445", "Diego Sistemas S.R.L.",
						"administracion@sistemas.example.test", "Mitre", 404, "Sucursal Oeste", 4,
						12000.0, "EMITIDA", 3, "ART-004", "Monitor", 4000.0,
						condicionIva, moneda, rubro, marca, listaPrecio, usuario));

		facturas.forEach(entityManager::persist);
		entityManager.flush();
	}

	private FacturaVenta crearFactura(Long numero, LocalDate fecha, String cuit, String denominacion,
			String email, String calle, int altura, String sucursal, int numeroPuntoVenta,
			double importeTotal, String estado, int cantidadItems, String codigoArticulo,
			String descripcionArticulo, double precioUnitario, CondicionIva condicionIva,
			TipoMoneda moneda, Rubro rubro, Marca marca, ListaPrecio listaPrecio, Usuario usuario) {
		Contacto contacto = new Contacto();
		contacto.setEmail(email);
		contacto.setTelefono("1100000000");
		contacto.setCelular("1500000000");
		entityManager.persist(contacto);

		Domicilio domicilio = new Domicilio();
		domicilio.setNombreCalle(calle);
		domicilio.setNumeroCalle(Integer.toString(altura));
		entityManager.persist(domicilio);

		Cliente cliente = new Cliente();
		cliente.setCuitCuil(cuit);
		cliente.setDenominacion(denominacion);
		cliente.setContacto(contacto);
		cliente.setDomicilio(domicilio);
		prepararAuditoria(cliente, usuario, fecha);
		entityManager.persist(cliente);

		PuntoVenta puntoVenta = new PuntoVenta();
		puntoVenta.setNumero(numeroPuntoVenta);
		puntoVenta.setDescripcion(sucursal);
		puntoVenta.setTipoEmision("ELECTRONICA");
		puntoVenta.setDomicilioComercial(calle + " " + altura);
		prepararAuditoria(puntoVenta, usuario, fecha);
		entityManager.persist(puntoVenta);

		Articulo articulo = new Articulo();
		articulo.setRubro(rubro);
		articulo.setCodigo(codigoArticulo);
		articulo.setDenominacion(descripcionArticulo);
		articulo.setMarca(marca);
		prepararAuditoria(articulo, usuario, fecha);
		entityManager.persist(articulo);

		ListaPrecioArticulo listaPrecioArticulo = new ListaPrecioArticulo();
		listaPrecioArticulo.setListaPrecio(listaPrecio);
		listaPrecioArticulo.setPrecioVenta(precioUnitario);
		listaPrecioArticulo.setArticulo(articulo);
		prepararAuditoria(listaPrecioArticulo, usuario, fecha);
		entityManager.persist(listaPrecioArticulo);

		FacturaVenta factura = new FacturaVenta();
		factura.setNumero(numero);
		factura.setFechaEmision(fecha(fecha));
		factura.setCliente(cliente);
		factura.setCondicionIva(condicionIva);
		factura.setTipoMoneda(moneda);
		factura.setPuntoVenta(puntoVenta);
		factura.setImporteCobrado(importeTotal);
		factura.setImporteSaldo(0.0);
		factura.setImporteTotal(importeTotal);
		factura.setCae("CAE-DEMO-" + numero);
		factura.setResultadoAfip("APROBADA");
		factura.setEstado(estado);
		factura.setObservaciones("Factura de demostracion " + numero);
		prepararAuditoria(factura, usuario, fecha);

		for (int item = 1; item <= cantidadItems; item++) {
			FacturaVentaDetalle detalle = new FacturaVentaDetalle();
			detalle.setListaPrecioArticulo(listaPrecioArticulo);
			detalle.setDescripcion(descripcionArticulo + " - item " + item);
			detalle.setCantidad(1.0);
			detalle.setPrecioUnitario(precioUnitario);
			detalle.setPorcentajeBonificacion(0.0);
			detalle.setImporteNeto(precioUnitario);
			detalle.setImporteIva(0.0);
			detalle.setImporteSubtotal(precioUnitario);
			factura.addDetalle(detalle);
		}
		return factura;
	}

	private void prepararAuditoria(AuditoriaApp entidad, Usuario usuario, LocalDate fecha) {
		Date instante = fecha(fecha);
		entidad.setFechaAlta(instante);
		entidad.setFechaModificacion(instante);
		entidad.setUsuarioCarga(usuario);
		entidad.setUsuarioModificacion(usuario);
	}

	private Date fecha(LocalDate fecha) {
		return Date.from(fecha.atStartOfDay().toInstant(ZoneOffset.UTC));
	}
}
