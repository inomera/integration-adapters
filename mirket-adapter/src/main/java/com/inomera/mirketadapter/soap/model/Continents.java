package com.inomera.mirketadapter.soap.model;


import generated.countryinfoservice.ListOfContinentsByNameResponse;
import generated.countryinfoservice.TContinent;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Continents implements Serializable {

  private final List<Continent> continents;

  public Continents(ListOfContinentsByNameResponse continentsByNameResponse) {
    this(continentsByNameResponse.getListOfContinentsByNameResult().getTContinent());
  }

  public Continents(List<TContinent> tContinents) {
    if (CollectionUtils.isEmpty(tContinents)) {
      this.continents = new ArrayList<>();
    } else {
      this.continents = tContinents.stream().map(Continent::new).toList();
    }
  }

  @Getter
  @ToString
  public class Continent implements Serializable {

    private final String code;
    private final String name;

    public Continent(TContinent continent) {
      this.code = continent.getSCode();
      this.name = continent.getSName();
    }
  }
}
