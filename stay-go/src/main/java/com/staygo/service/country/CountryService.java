package com.staygo.service.country;

import com.staygo.enity.weather.Country;
import com.staygo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getByName(String name) {
        return countryRepository.findByName(name).orElse(null);
    }

    public Country getById(Long id) {
        return countryRepository.findById(id).orElse(null);
    }

    public Iterable<Country> getAll() {
        return countryRepository.findAll();
    }

    public Boolean getByObject(Country country) {
        return countryRepository.existsByLatitudeAndLongitude(country.getLatitude(), country.getLongitude());
    }

    public void saveCountry(Country country) {
        countryRepository.save(country);
    }
}