package com.testejava.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PedidoDTO {
    private String numeroControle;
    private LocalDate dataCadastro;
    private String nome;
    private BigDecimal valor;
    private Integer quantidade;
    private Long codigoCliente;
    private BigDecimal valorTotal;
}
