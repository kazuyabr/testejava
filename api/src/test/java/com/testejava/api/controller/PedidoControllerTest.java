package com.testejava.api.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testejava.api.entities.Pedidos;
import com.testejava.api.services.PedidosService;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidosService pedidosService;

    @Test
    public void testCriarPedido() throws Exception {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(2);
        pedido.setCodigoCliente(1L);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedido)))
                .andExpect(status().isCreated());

        verify(pedidosService, times(1)).salvarPedido(pedido);
    }

    @Test
    public void testBuscarPedidos() throws Exception {
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
        pedido2.setQuantidade(4);
        pedido2.setCodigoCliente(2L);

        List<Pedidos> pedidosMock = Arrays.asList(pedido1, pedido2);

        when(pedidosService.buscarPedidos(null, null)).thenReturn(pedidosMock);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroControle").value("123"))
                .andExpect(jsonPath("$[1].numeroControle").value("456"));

        verify(pedidosService, times(1)).buscarPedidos(null, null);
    }

    @Test
    public void testSalvarPedido() throws Exception {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("789");
        pedido.setNome("Produto 3");
        pedido.setValor(BigDecimal.valueOf(300));
        pedido.setQuantidade(2);
        pedido.setCodigoCliente(3L);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedido)))
                .andExpect(status().isCreated());

        verify(pedidosService, times(1)).salvarPedido(pedido);
    }

    @Test
    public void testSalvarPedidoComErro() throws Exception {
        Pedidos pedido = new Pedidos();
        pedido.setNumeroControle("123");
        pedido.setNome("Produto 1");
        pedido.setValor(BigDecimal.valueOf(100));
        pedido.setQuantidade(2);
        pedido.setCodigoCliente(1L);

        doThrow(new RuntimeException("Número de controle já cadastrado"))
                .when(pedidosService).salvarPedido(pedido);

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pedido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Número de controle já cadastrado"));

        verify(pedidosService, times(1)).salvarPedido(pedido);
    }
}