package br.com.cardif.jasper_report.controller;

import br.com.cardif.jasper_report.dto.FileReportDTO;
import br.com.cardif.jasper_report.dto.SearchPaymentSolicitationsExternalPdfDTO;
import br.com.cardif.jasper_report.service.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class JasperReportController {

    @Autowired
    JasperReportService jasperReportService;

    @PostMapping(value = "/search/pdf")
    public ResponseEntity<List<FileReportDTO>> getExternalPaymentPdf(@RequestBody SearchPaymentSolicitationsExternalPdfDTO reportRequestDTO,
                                                                     @RequestParam String tipo,
                                                                     @RequestParam Boolean signature) {

        try {
            List<FileReportDTO> pdfBase64List = jasperReportService.getExternalPaymentPdf(reportRequestDTO, tipo, signature);

            return ResponseEntity.ok().body(pdfBase64List);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
