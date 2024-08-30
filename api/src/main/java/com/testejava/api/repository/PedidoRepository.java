package com.testejava.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testejava.api.entities.Pedidos;

@Repository
public interface PedidoRepository extends JpaRepository<Pedidos, Long> {
    boolean existsByNumeroControle(String numeroControle);
    List<Pedidos> findByNumeroControle(String numeroControle);
    List<Pedidos> findByDataCadastro(LocalDate dataCadastro);
}

