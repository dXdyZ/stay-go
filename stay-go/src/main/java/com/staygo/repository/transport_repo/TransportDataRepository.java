package com.staygo.repository.transport_repo;

import com.staygo.enity.transport.TransportData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportDataRepository extends CrudRepository<TransportData, Long> {
}
