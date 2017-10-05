package javaday.istanbul.sliconf.micro.config;

import java.util.HashMap;
import spark.Filter;
import spark.Spark;

/**
 * Spark Server'in cross-origin olarak calisabilmesi icin her response sonrasi
 * eklenmesi gereken header bilgisini barindiran ve spark methodlari ile bu headerleri
 * response'a ekleyen sinif
 */
public final class CorsFilter {

    private static final HashMap<String, String> corsHeaders = new HashMap<>();

    private CorsFilter() {}
    
    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,application/json");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    public static void apply() {
        Filter filter = (request, response) -> corsHeaders.forEach(response::header);
        Spark.after(filter);
    }}