package activities;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import api.ApiClient;
import api.ApiService;
import models.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(itson.edu.rentamanteleria.R.layout.activity_main);

        probarAPI();
    }

    private void probarAPI() {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        Call<List<Producto>> call = api.getProductos();

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {

                if (response.isSuccessful()) {
                    List<Producto> productos = response.body();

                    for (Producto p : productos) {
                        Log.d("API_TEST", "Producto: " + p.getNombre());
                    }

                } else {
                    Log.e("API_ERROR", "Error en respuesta");
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}