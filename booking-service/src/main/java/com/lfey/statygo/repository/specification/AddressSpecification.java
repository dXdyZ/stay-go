package com.lfey.statygo.repository.specification;

import com.lfey.statygo.entity.Address;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecification {
    public static Specification<Address> hasCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (country == null) return null;
            return criteriaBuilder.equal(root.get("country"), country);
        };
    }

    public static Specification<Address> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) return null;
            return criteriaBuilder.equal(root.get("city"), city);
        };
    }

    public static Specification<Address> hasStreet(String street) {
        return (root, query, criteriaBuilder) -> {
            if (street == null) return null;
            return criteriaBuilder.equal(root.get("street"), street);
        };
    }

    public static Specification<Address> hasHouseNumber(String houseNumber) {
        return (root, query, criteriaBuilder) -> {
            if (houseNumber == null) return null;
            return criteriaBuilder.equal(root.get("houseNumber"), houseNumber);
        };
    }
}
