package com.korporativo.korporativo_backend.repository;

import com.korporativo.korporativo_backend.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    List<Presupuesto> findByClienteId(Long clienteId);

    @Query("SELECT COALESCE(SUM(p.precio), 0) FROM Presupuesto p WHERE p.cliente.id = :clienteId")
    Double sumPrecioByClienteId(@Param("clienteId") Long clienteId);
}
