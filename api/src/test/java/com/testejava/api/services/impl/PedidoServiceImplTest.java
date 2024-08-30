package com.testejava.api.services.impl;

import com.testejava.api.entities.Pedidos;
import com.testejava.api.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceImplTest {

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Test
    public void testSalvarPedido() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(2);
        pedido.setCodigoCliente(1L);

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(false);

        pedidoService.salvarPedido(pedido);

        assertEquals(LocalDate.now(), pedido.getDataCadastro());
        assertEquals(BigDecimal.valueOf(200), pedido.getValorTotal());

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testSalvarPedidoComNumeroControleExistente() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> pedidoService.salvarPedido(pedido));

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    public void testSalvarPedidoComDataCadastroNula() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(2);
        pedido.setCodigoCliente(1L);

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(false);

        pedidoService.salvarPedido(pedido);

        assertNotNull(pedido.getDataCadastro());
        assertEquals(LocalDate.now(), pedido.getDataCadastro());

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testSalvarPedidoComQuantidadeNula() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setCodigoCliente(1L);

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(false);

        pedidoService.salvarPedido(pedido);

        assertEquals(1, pedido.getQuantidade());
        assertEquals(BigDecimal.valueOf(100), pedido.getValorTotal());

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testSalvarPedidoComDescontoDe5Porcento() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(5);
        pedido.setCodigoCliente(1L);

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(false);

        pedidoService.salvarPedido(pedido);

        assertEquals(0, pedido.getValorTotal().compareTo(BigDecimal.valueOf(475)));

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testSalvarPedidoComDescontoDe10Porcento() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(10);
        pedido.setCodigoCliente(1L);

        when(pedidoRepository.existsByNumeroControle("123")).thenReturn(false);

        pedidoService.salvarPedido(pedido);

        assertEquals(0, pedido.getValorTotal().compareTo(BigDecimal.valueOf(900)));

        verify(pedidoRepository, times(1)).existsByNumeroControle("123");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    public void testBuscarPedidos() {
        Pedidos pedido1 = new Pedidos();
        pedido1.setNumeroControle("123");
        pedido1.setNome("Produto 1");
        pedido1.setValor(BigDecimal.valueOf(100));
        pedido1.setQuantidade(2);
        pedido1.setCodigoCliente(1L);

        Pedidos pedido2 = new Pedidos();
        pedido2.setNumeroControle("456");
        pedido2.setNome("Produto 2");
        pedido2.setValor(BigDecimal.valueOf(200));
        pedido2.setQuantidade(3);
        pedido2.setCodigoCliente(2L);

        List<Pedidos> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedidos> result = pedidoService.buscarPedidos(null, null);

        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getNumeroControle());
        assertEquals("456", result.get(1).getNumeroControle());

        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPedidosPorNumeroControle() {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");

        when(pedidoRepository.findByNumeroControle("123")).thenReturn(Arrays.asList(pedido));

        List<Pedidos> result = pedidoService.buscarPedidos("123", null);

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getNumeroControle());

        verify(pedidoRepository, times(1)).findByNumeroControle("123");
        verify(pedidoRepository, never()).findByDataCadastro(any());
        verify(pedidoRepository, never()).findAll();
    }

    @Test
    public void testBuscarPedidosPorDataCadastro() {
        LocalDate dataCadastro = LocalDate.now();
        Pedidos pedido = new Pedidos();
        pedido.setDataCadastro(dataCadastro);

        when(pedidoRepository.findByDataCadastro(dataCadastro)).thenReturn(Arrays.asList(pedido));

        List<Pedidos> result = pedidoService.buscarPedidos(null, dataCadastro);

        assertEquals(1, result.size());
        assertEquals(dataCadastro, result.get(0).getDataCadastro());

        verify(pedidoRepository, never()).findByNumeroControle(any());
        verify(pedidoRepository, times(1)).findByDataCadastro(dataCadastro);
        verify(pedidoRepository, never()).findAll();
    }
}