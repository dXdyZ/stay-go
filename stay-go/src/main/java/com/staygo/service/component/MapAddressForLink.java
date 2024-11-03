package com.staygo.service.component;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MapAddressForLink {

    public String mappingAddressForAirport(String name, String street, String city, String country, String houseNumber) {
        List<String> allParam = new ArrayList<>() {{
            add(name);
            add(street);
            add(city);
            add(country);
            add(houseNumber);
        }};
        List<String> mapList = allParam.stream()
                .filter(Objects::nonNull)
                .filter(p -> !p.isEmpty())
                .toList();
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < mapList.size() - 1; i++) {
            message.append(mapList.get(i)).append(",+");
        }
        message.append(mapList.get(mapList.size() - 1));
        return message.toString();
    }
}
