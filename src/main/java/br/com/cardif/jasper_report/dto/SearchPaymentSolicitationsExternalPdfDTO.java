package br.com.cardif.jasper_report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPaymentSolicitationsExternalPdfDTO {

    private Long solicitationId;
    private String cnpj;
    private String status;

}
