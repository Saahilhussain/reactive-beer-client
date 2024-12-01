package guru.springframework.reactivebeerclient.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.reactivebeerclient.config.WebClientProperties;
import guru.springframework.reactivebeerclient.model.BeerDto;
import guru.springframework.reactivebeerclient.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt on 3/13/21.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    @Override
    public Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(WebClientProperties.UPDATE_PATH)
                     .queryParamIfPresent("showInventoryOnHand",Optional.ofNullable(showInventoryOnHand))
                     .build(id.toString())
                ).retrieve()
            .bodyToMono(BeerDto.class);
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(WebClientProperties.LIST_PATH).queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnhand))
            .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber)).build())
            .retrieve()
            .bodyToMono(BeerPagedList.class);

    }

    @Override
    public Mono<ResponseEntity<Void>> createBeer(BeerDto beerDto) {

            return webClient.post()
                .uri(WebClientProperties.LIST_PATH)
                .body(BodyInserters.fromValue(beerDto))
                .retrieve()
                .toBodilessEntity();

    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(BeerDto beerDto) {
        return webClient.put()
            .uri(WebClientProperties.UPDATE_PATH,beerDto.getId())
            .body(BodyInserters.fromValue(beerDto))
            .retrieve()
            .toBodilessEntity();

    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeerById(UUID id) {
        return webClient.delete()
            .uri(WebClientProperties.UPDATE_PATH,id)
            .retrieve()
            .toBodilessEntity();
    }

    @Override
    public Mono<BeerDto> getBeerByUPC(String upc) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(WebClientProperties.UPC_PATH).build(upc))
            .retrieve().bodyToMono(BeerDto.class);
    }
}
