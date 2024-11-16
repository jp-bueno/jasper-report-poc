package br.com.cardif.jasper_report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileReportDTO {

    private String content;
    private Long claimId;
    private String fileName; // [ID da AT] - Solicitação de Pagamento - [Id da Sol. Pagto]
    private String fileType;
}
