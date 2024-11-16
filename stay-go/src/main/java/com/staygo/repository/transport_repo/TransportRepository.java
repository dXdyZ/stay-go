package com.staygo.repository.transport_repo;

import com.staygo.enity.transport.Transport;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportRepository extends CrudRepository<Transport, Long> {
    List<Transport> findAllByTransportNameAndAddress_CountryAndAddress_City(String transportName, @NotNull @Size(
            min = 2,
            max = 200,
            message = "Требуется название страны, максимум 200 символов"
    ) String addressCountry, @NotNull @Size(
            min = 2,
            max = 255,
            message = "Требуетяс название города, максимум 255 символов"
    ) String addressCity);
}

