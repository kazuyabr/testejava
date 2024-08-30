package com.testejava.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.testejava.api.dto.PedidoDTO;
import com.testejava.api.entities.Pedidos;
import com.testejava.api.services.PedidosService;

import java.net.URI;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidosService pedidoService;

    public PedidoController(PedidosService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"})
    public ResponseEntity<String> salvarPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedidos pedido = new Pedidos();
            pedido.setNumeroControle(pedidoDTO.getNumeroControle());
            pedido.setDataCadastro(pedidoDTO.getDataCadastro());
            pedido.setNome(pedidoDTO.getNome());
            pedido.setValor(pedidoDTO.getValor());
            pedido.setQuantidade(pedidoDTO.getQuantidade());
            pedido.setCodigoCliente(pedidoDTO.getCodigoCliente());

            pedidoService.salvarPedido(pedido);

            return ResponseEntity.created(URI.create("/pedidos/" + pedido.getId())).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public List<PedidoDTO> buscarPedidos(@RequestParam(required = false) String numeroPedido,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCadastro) {
        List<Pedidos> pedidos = pedidoService.buscarPedidos(numeroPedido, dataCadastro);

        return pedidos.stream()
                .map(pedido -> {
                    PedidoDTO pedidoDTO = new PedidoDTO();
                    pedidoDTO.setNumeroControle(pedido.getNumeroControle());
                    pedidoDTO.setDataCadastro(pedido.getDataCadastro());
                    pedidoDTO.setNome(pedido.getNome());
                    pedidoDTO.setValor(pedido.getValor());
                    pedidoDTO.setQuantidade(pedido.getQuantidade());
                    pedidoDTO.setCodigoCliente(pedido.getCodigoCliente());
                    pedidoDTO.setValorTotal(pedido.getValorTotal());
                    return pedidoDTO;
                })
                .collect(Collectors.toList());
    }
}

