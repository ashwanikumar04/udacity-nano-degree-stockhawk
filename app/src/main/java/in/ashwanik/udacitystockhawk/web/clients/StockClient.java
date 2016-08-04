package in.ashwanik.udacitystockhawk.web.clients;

import java.util.List;

import in.ashwanik.udacitystockhawk.entities.Quote;
import in.ashwanik.udacitystockhawk.entities.QuoteResponse;
import in.ashwanik.udacitystockhawk.web.ApiUrls;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AshwaniK on 7/30/2016.
 */
public interface StockClient {
    @GET(ApiUrls.BASE_API_URL)
    Call<QuoteResponse<Quote>> getQuote(@Query("q") String query, @Query("format") String format, @Query("env") String env);

    @GET(ApiUrls.BASE_API_URL)
    Call<QuoteResponse<List<Quote>>> getQuotes(@Query("q") String query, @Query("format") String format, @Query("env") String env);
}
