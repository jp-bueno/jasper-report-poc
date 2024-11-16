package br.com.cardif.jasper_report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private String senha;
    private String descricao;
    private String nrOs;
    private String quantidade;
    private String valorUnitario;
    private String valorTotal;
    private String codigoFabric;
}
