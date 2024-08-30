package com.testejava.api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.testejava.api.entities.Pedidos;
import com.testejava.api.repository.PedidoRepository;
import com.testejava.api.services.PedidosService;

@Service
public class PedidoServiceImpl implements PedidosService {
    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void salvarPedido(Pedidos pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        if (codigoCliente == null || codigoCliente < 1 || codigoCliente > 10) {
            throw new RuntimeException("Código do cliente inválido");
        }

        long count = pedidoRepository.count();
        if (count >= 10) {
            throw new RuntimeException("Limite de 10 pedidos atingido");
        }

        if (pedidoRepository.existsByNumeroControle(pedido.getNumeroControle())) {
            throw new RuntimeException("Número de controle já cadastrado");
        }

        if (pedido.getDataCadastro() == null) {
            pedido.setDataCadastro(LocalDate.now());
        }

        if (pedido.getQuantidade() == null) {
            pedido.setQuantidade(1);
        }

        BigDecimal valorTotal = pedido.getValor().multiply(BigDecimal.valueOf(pedido.getQuantidade()));

        if (pedido.getQuantidade() >= 10) {
            valorTotal = valorTotal.multiply(BigDecimal.valueOf(0.9));
        } else if (pedido.getQuantidade() >= 5) {
            valorTotal = valorTotal.multiply(BigDecimal.valueOf(0.95));
        }

        pedido.setValorTotal(valorTotal);

        pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedidos> buscarPedidos(String numeroPedido, LocalDate dataCadastro) {
        if (numeroPedido != null) {
            return pedidoRepository.findByNumeroControle(numeroPedido);
        } else if (dataCadastro != null) {
            return pedidoRepository.findByDataCadastro(dataCadastro);
        } else {
            return pedidoRepository.findAll();
        }
    }
}