package com.staygo.repository.transport_repo;

import com.staygo.enity.transport.Transport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends CrudRepository<Transport, Long> {
}
