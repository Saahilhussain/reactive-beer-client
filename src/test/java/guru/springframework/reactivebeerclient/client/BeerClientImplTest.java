package guru.springframework.reactivebeerclient.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.reactivebeerclient.config.WebClientConfig;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import guru.springframework.reactivebeerclient.model.v2.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class BeerClientImplTest {

  BeerClientImpl beerClient;

  @BeforeEach
  void setUp () {

    beerClient = new BeerClientImpl(new WebClientConfig().webClient());


  }


  @Test
  void listBeers () {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null,
                                                                 null);

    BeerPagedList pagedList = beerPagedListMono.block();

    assertThat(pagedList).isNotNull();
    assertThat(pagedList.getContent().size()).isEqualTo(10);
  }


  @Disabled("API returning inventory when should not be")
  @Test
  void getBeerById() {
    Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null,
                                                                 false);

    BeerPagedList pagedList = beerPagedListMono.block();

    UUID beerId = pagedList.getContent().get(0).getId();

    Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerId, false);

    BeerDto beerDto = beerDtoMono.block();

    System.out.println(beerDto);
  }



  @Test
  void createBeer () {
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(BeerDto.builder()
                                                                              .beerName("Galaxy Cat v.2")
                                                                              .beerStyle(BeerStyleEnum.ALE.toString())
                                                                              .upc("9122089364981")
                                                                              .price(BigDecimal.valueOf(
                                                                                  100.0))
                                                                              .build());

    assertEquals(responseEntityMono.block().getStatusCodeValue(),201);
    System.out.println(responseEntityMono.block().getHeaders().getLocation());
  }


  @Test
  void updateBeer () {
//    "beerName": "Galaxy Cat",
//        "beerStyle": "PALE_ALE",
//        "upc": "9122089364369",
//        "price": 89.63,
//        "quantityOnHand": 1808,
    Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(
        BeerDto.builder()
            .beerName("Galaxy Cat")
            .beerStyle(BeerStyleEnum.LAGER.toString())
            .upc("9122089364369")
            .price(BigDecimal.valueOf(89.63))
            .quantityOnHand(1809)
            .build()
    );
    assertEquals(responseEntityMono.block().getStatusCodeValue(),204);

  }


  @Test
  void deleteBeerById () {

    Mono<ResponseEntity<Void>> response = beerClient.deleteBeerById(UUID.fromString("a873afb4-ab54-4bc5-b8fa-b600926897cc"));

    assertEquals(response.block().getStatusCodeValue(),204);
  }


  @Test
  void getBeerByUPC () {
    Mono<BeerPagedList> listResponse = beerClient.listBeers(null,null,null,null,null);
    BeerDto firstBeer = listResponse.block().getContent().get(0);

    Mono<BeerDto> response = beerClient.getBeerByUPC(firstBeer.getUpc());
    assertEquals(response.block().getBeerName(),firstBeer.getBeerName());
    assertEquals(response.block().getId(),firstBeer.getId());
  }
}