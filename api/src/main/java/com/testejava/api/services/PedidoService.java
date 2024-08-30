package com.testejava.api.services;

import java.time.LocalDate;
import java.util.List;

import com.testejava.api.entities.Pedido;

public interface PedidoService {
    void salvarPedido(Pedido pedido);
    List<Pedido> buscarPedidos(String numeroPedido, LocalDate dataCadastro);
}

