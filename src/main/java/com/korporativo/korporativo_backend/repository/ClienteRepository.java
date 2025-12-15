package com.korporativo.korporativo_backend.repository;

import com.korporativo.korporativo_backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Consulta personalizada de ejemplo
    List<Cliente> findByEmailContainingIgnoreCase(String email);

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

}
