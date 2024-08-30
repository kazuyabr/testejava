package com.testejava.api.services;

import java.time.LocalDate;
import java.util.List;

import com.testejava.api.entities.Pedidos;

public interface PedidosService {
    void salvarPedido(Pedidos pedidos);
    List<Pedidos> buscarPedidos(String numeroPedido, LocalDate dataCadastro);
}

