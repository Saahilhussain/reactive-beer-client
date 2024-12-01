package guru.springframework.reactivebeerclient.config;


public class WebClientProperties {

  public static final String BEER_V1 = "http://api.springframework.guru";


  public static final String LIST_PATH = "/api/v1/beer";
  public static final String UPDATE_PATH = "/api/v1/beer/{beerId}";

  public static final String UPC_PATH = "/api/v1/beerUpc/{upc}";
}
