package br.com.cardif.jasper_report.service;

import br.com.cardif.jasper_report.dto.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static br.com.cardif.jasper_report.utils.Utils.convetToStringFromInteger;
import static br.com.cardif.jasper_report.utils.Utils.formatDecimals;

@Service
public class JasperReportService {

    @Value("${report.jasper.path}")
    private String reportPath;

    public List<FileReportDTO> getExternalPaymentPdf(SearchPaymentSolicitationsExternalPdfDTO reportRequestDTO, String tipo, Boolean signature)
            throws JRException, IOException {

        // Todo - call Query DATABASE HERE:

        List<PaymentSolicitations> paymentSolicitations = new ArrayList<>();
        PaymentSolicitations payment = PaymentSolicitations.builder()
                .id(7815l)
                .atId(4553l)
                .invoiceNumber(384l)
                .invoiceValue(Double.valueOf(145.99))
                .invoiceEmissionDate("04/04/2013")
                .invoiceSubmissionDate("05/04/2013")
                .invoiceReceiptDate("12/04/2013")
                .status("Aprovada")
                .siglaDespesa("MB")
                .usuarioUpd("moreirami")
                .descripcion("SERMAGEL PECAS E SERVICOS LTDA ME")
                .build();
        paymentSolicitations.add(payment);

        List<ItemsPaymentSolicitations> itemsPaymentSolicitations = new ArrayList<>();
        ItemsPaymentSolicitations itemsPay = ItemsPaymentSolicitations.builder()
                .itemId(1l)
                .id(7815l)
                .quotationId(1l)
                .description("Mão de Obra")
                .quantity(1)
                .unitaryValue(Double.valueOf(145.99))
                .totalValue(Double.valueOf(1456.78))
                .manufacturerCode("")
                .ownerId(55872l)
                .ticket("T055872")
                .expenseAcronym("MB")
                .status("Aprovada")
                .osId("011")
                .build();

        for (int i = 0; i < 120; i++) {
            itemsPaymentSolicitations.add(itemsPay);
        }
//        itemsPaymentSolicitations.add(itemsPay);

        return generateReports(paymentSolicitations, itemsPaymentSolicitations, tipo, signature);
    }

    private List<FileReportDTO> generateReports(List<PaymentSolicitations> paymentSolicitations, List<ItemsPaymentSolicitations> items, String tipo, Boolean signature)
            throws JRException, IOException {

        List<FileReportDTO> fileReportDTOList = new ArrayList<>();

        for (PaymentSolicitations payment : paymentSolicitations) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("SUBREPORT_DIR", new ClassPathResource("reports/solicitacao_de_pagamento_sub.jasper").getFile().getAbsolutePath());
            parameters.put("tipo", tipo);
            parameters.put("idSolPagto", payment.getId().toString());
            parameters.put("razaoSocial", payment.getDescripcion());
            parameters.put("dateInsSolPagto", payment.getInvoiceEmissionDate());
            parameters.put("valorTotalSolPagto", formatDecimals(payment.getInvoiceValue()));
            parameters.put("numeroNF", payment.getInvoiceNumber().toString());
            parameters.put("dataImpressao", converToLocalDatePTBR(LocalDate.now()));

            if (Boolean.TRUE.equals(signature)) {
                parameters.put("signature", true);
            }

            List<ItemDTO> listItems = new ArrayList<>();
            items.forEach(lsItem -> {
                ItemDTO ordem = buildItemsPayment(lsItem);
                listItems.add(ordem);
            });

            AtomicReference<Double> valorTotal = new AtomicReference<>(0.0);
            listItems.forEach(ls -> valorTotal.updateAndGet(v -> v + Double.parseDouble(ls.getValorTotal())));
            parameters.put("subTotal", formatDecimals(valorTotal.get()));
            parameters.put("totalGeral", formatDecimals(valorTotal.get()));

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);
            parameters.put("CollectionBeanParam", itemsJRBean);

            fileReportDTOList.add(buildReportDTO(payment, parameters, new PaymentsReportDTO(listItems)));
        }

        return fileReportDTOList;
    }

    private static ItemDTO buildItemsPayment(ItemsPaymentSolicitations lsItem) {
        return ItemDTO.builder()
                .senha(lsItem.getTicket())
                .descricao(lsItem.getDescription())
                .nrOs(lsItem.getOsId())
                .quantidade(convetToStringFromInteger(lsItem.getQuantity()))
                .valorUnitario(formatDecimals(lsItem.getUnitaryValue()))
                .valorTotal(formatDecimals(lsItem.getTotalValue()))
                .codigoFabric(lsItem.getManufacturerCode())
                .build();
    }

    private String generatePdf(Map<String, Object> parameters, PaymentsReportDTO paymentsReportDTO) throws JRException, FileNotFoundException {
        InputStream input = new FileInputStream(reportPath);
        JasperDesign jasperDesign = JRXmlLoader.load(input);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanArrayDataSource(new PaymentsReportDTO[]{paymentsReportDTO}));

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfStream);

        byte[] pdfBytes = pdfStream.toByteArray();
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    private static String converToLocalDatePTBR(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    private FileReportDTO buildReportDTO(PaymentSolicitations payment, Map<String, Object> parameters, PaymentsReportDTO paymentsReportDTO) throws JRException, FileNotFoundException {
        return FileReportDTO.builder()
                .content(generatePdf(parameters, paymentsReportDTO))
                .claimId(payment.getId())
                .fileName(buildFileName(payment.getAtId(), payment.getId()))
                .fileType("PDF")
                .build();
    }

    private String buildFileName(Long atId, Long id) {
        return atId + " - Solicitação de Pagamento - " + id;
    }
}
