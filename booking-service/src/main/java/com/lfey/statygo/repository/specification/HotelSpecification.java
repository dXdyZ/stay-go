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
            return criteriaBuilder.equal(root.get("country"), country);
        };
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null) return null;
            Join<Hotel, Address> addressJoin = root.join("address");
            return criteriaBuilder.equal(root.get("city"), city);
        };
    }

    public static Specification<Hotel> hasStreet(String street) {
        return (root, query, criteriaBuilder) -> {
            if (street == null) return null;
            Join<Hotel, Address> addressJoin = root.join("address");
            return criteriaBuilder.equal(root.get("street"), street);
        };
    }

    public static Specification<Hotel> hasHouseNumber(String houseNumber) {
        return (root, query, criteriaBuilder) -> {
            if (houseNumber == null) return null;
            Join<Hotel, Address> addressJoin = root.join("address");
            return criteriaBuilder.equal(root.get("houseNumber"), houseNumber);
        };
    }
}
