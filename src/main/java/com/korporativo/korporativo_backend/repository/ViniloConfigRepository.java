package com.korporativo.korporativo_backend.repository;

import com.korporativo.korporativo_backend.model.ViniloConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViniloConfigRepository extends JpaRepository<ViniloConfig, Long> {

    List<ViniloConfig> findByPresupuestoId(Long presupuestoId);

    Long countByPresupuestoId(Long presupuestoId);
}
