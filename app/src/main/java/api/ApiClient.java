package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Si usas emulador, usa 10.0.2.2. Si usas dispositivo físico, usa la IP de tu PC.
    // Reemplaza 10.0.2.2 por la IP real de tu computadora
    //192.168.1.32
    private static final String BASE_URL = "http://192.168.1.43:3000/";
    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
