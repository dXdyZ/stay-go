package com.lfey.statygo.repository.specification;

import com.lfey.statygo.entity.Address;
import com.lfey.statygo.entity.Hotel;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class HotelSpecification {

    public static Specification<Hotel> hasStars(Integer stars) {
        return (root, query, criteriaBuilder) -> {
            if (stars == null) return null;
            return criteriaBuilder.equal(root.get("stars"), stars);
        };
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (country == null) return null;
            Join<Hotel, Address> addressJoin = root.join("address");
            return criteriaBuilder.equal(addressJoin.get("country"), country);
        };
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) return null;
            Join<Hotel, Address> addressJoin = root.join("address");
            return criteriaBuilder.equal(addressJoin.get("city"), city);
        };
    }
}
