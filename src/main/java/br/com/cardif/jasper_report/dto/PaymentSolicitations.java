package br.com.cardif.jasper_report.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSolicitations {

    @Id
    @Column(name = "id_solpag")
    private Long id;
    @Column(name = "assigned_to_id")
    private Long atId;
    @Column(name = "numero_nf")
    private Long invoiceNumber;
    @Column(name = "valor_nf")
    private Double invoiceValue;
    @Column(name = "DATA_EMIS_NF")
    private String invoiceEmissionDate;
    @Column(name = "DATA_ENVIO_NF")
    private String invoiceSubmissionDate;
    @Column(name = "DATA_RECEP_NF")
    private String invoiceReceiptDate;
    @Column(name = "status")
    private String status;
    @Column(name = "sigla_despesa")
    private String siglaDespesa;
    @Column(name = "usuario_upd")
    private String usuarioUpd;
    @Column(name = "descripcion")
    private String descripcion; // tá errado a nomenclatura no banco
}
