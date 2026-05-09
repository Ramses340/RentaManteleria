package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Si usas emulador, usa 10.0.2.2. Si usas dispositivo físico, usa la IP de tu PC.
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
