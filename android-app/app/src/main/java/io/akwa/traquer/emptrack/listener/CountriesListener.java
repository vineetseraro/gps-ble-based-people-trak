package io.akwa.traquer.emptrack.listener;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.CountryApiResponse;

public interface CountriesListener {
    void onCountriesResponse(CountryApiResponse response, NicbitException e);
}
