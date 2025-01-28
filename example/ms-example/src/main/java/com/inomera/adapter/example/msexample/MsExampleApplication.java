package com.inomera.adapter.example.msexample;

import com.inomera.integration.model.AdapterResponse;
import com.inomera.mirketadapter.rest.MirketAdapter;
import com.inomera.mirketadapter.soap.CountryAdapter;
import com.inomera.mirketadapter.soap.model.Continents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
public class MsExampleApplication {

    @Autowired
    private MirketAdapter mirketAdapter;

    @Autowired
    private CountryAdapter countryAdapter;

    public static void main(String[] args) {
        SpringApplication.run(MsExampleApplication.class, args);
    }

    @RestController
    public class MsExampleController {

        @GetMapping("/mirket")
        public String helloMirket() {
            AdapterResponse<String> result = mirketAdapter.getFirst(null, null);
            LOG.info("Mirket response: {}", result.toString());
            return result.getData();
        }

        @GetMapping("/country")
        public String helloCountry() {
            AdapterResponse<Continents> continentsAdapterResponse = countryAdapter.listOfContinentsByName(null, null);
            LOG.info("Country response: {}", continentsAdapterResponse.getData().getContinents());
            return continentsAdapterResponse.toString();
        }
    }

}
