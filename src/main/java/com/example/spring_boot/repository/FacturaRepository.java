package com.example.spring_boot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring_boot.dto.FacturaReporteDTO;
import com.example.spring_boot.model.FacturaVenta;

public interface FacturaRepository extends JpaRepository<FacturaVenta, Long> {

	@Query("""
			select new com.example.spring_boot.dto.FacturaReporteDTO(
				f.numero,
				f.fechaEmision,
				coalesce(c.denominacion, 'Consumidor Final'),
				ci.denominacion,
				pv.descripcion,
				f.importeTotal,
				count(d))
			from FacturaVenta f
			left join f.cliente c
			join f.condicionIva ci
			join f.puntoVenta pv
			left join f.detalles d
			where (:fechaDesde is null or f.fechaEmision >= :fechaDesde)
			  and (:fechaHastaExclusiva is null or f.fechaEmision < :fechaHastaExclusiva)
			  and (:estado is null or upper(f.estado) = upper(:estado))
			  and (:montoMinimo is null or f.importeTotal >= :montoMinimo)
			group by f.id, f.numero, f.fechaEmision, c.denominacion,
				ci.denominacion, pv.descripcion, f.importeTotal
			order by f.fechaEmision, f.numero
			""")
	List<FacturaReporteDTO> buscarReporte(
			@Param("fechaDesde") Date fechaDesde,
			@Param("fechaHastaExclusiva") Date fechaHastaExclusiva,
			@Param("estado") String estado,
			@Param("montoMinimo") Double montoMinimo);
}