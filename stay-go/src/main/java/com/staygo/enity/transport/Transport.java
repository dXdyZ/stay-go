package com.staygo.enity.transport;

import com.staygo.enity.address.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String transportName;
    private String status;
    private BigDecimal price;

    @OneToMany
    private List<ArmoredTransport> armoredTransport;

    @OneToMany(mappedBy = "transport")
    private List<TransportData> transportData;

    @OneToOne
    private Address address;
}
