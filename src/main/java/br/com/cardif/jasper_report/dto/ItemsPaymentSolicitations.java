package br.com.cardif.jasper_report.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsPaymentSolicitations {

    @Column(name = "itemId")
    private Long itemId;
    @Column(name = "id")
    private Long id;
    @Column(name = "quotationId")
    private Long quotationId;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unitaryValue")
    private Double unitaryValue;
    @Column(name = "totalValue")
    private Double totalValue;
    @Column(name = "manufacturerCode")
    private String manufacturerCode;
    @Column(name = "ownerId")
    private Long ownerId;
    @Column(name = "ticket")
    private String ticket;
    @Column(name = "expenseAcronym")
    private String expenseAcronym;
    @Column(name = "status")
    private String status;
    @Column(name = "osId")
    private String osId;
    @Column(name = "valueId")
    private Long valueId;

}