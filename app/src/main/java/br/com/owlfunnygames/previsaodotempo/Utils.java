package br.com.owlfunnygames.previsaodotempo;


/**
 * Created by alexandre on 07/08/16.
 */

public class Utils {

    //public static final String API_KEY = "de0c7357d361be8987b08ef3354969bb";
    //todo trocar chave (criar uma conta no OpenWeatherMap e pegar uma chave nova)
    public static final String API_KEY = "71a133bbb7f444fa7acf757df6e4b3ed";
    public static final String URL_PREVISOES = "http://api.openweathermap.org/data/2.5/forecast/daily?q=vitoria,brazil&mode=json&lang=pt&units=metric&cnt=14&APPID="+API_KEY;
    public static final String URL_ICONE = "http://openweathermap.org/img/w/";
    public static final String URL_ICONE2  = "http://openweathermap.org/img/w/10d.png";


}
