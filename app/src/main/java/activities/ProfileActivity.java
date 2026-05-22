package activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextInputEditText etDireccion, etNotas, etPreferencias;
    private BottomNavigationView bottomNav;
    private int idUsuario;
    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        ivProfile = findViewById(R.id.ivProfilePic);
        etDireccion = findViewById(R.id.etDireccion);
        etNotas = findViewById(R.id.etNotas);
        etPreferencias = findViewById(R.id.etPreferencias);
        bottomNav = findViewById(R.id.bottom_navigation);
        Button btnChangePic = findViewById(R.id.btnChangePic);
        Button btnSave = findViewById(R.id.btnSaveProfile);
        Button btnLogout = findViewById(R.id.btnLogout);

        cargarDatosPerfil();
        setupBottomNav();

        btnChangePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 100);
        });

        btnSave.setOnClickListener(v -> guardarCambios());

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("RentaPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_products) {
                startActivity(new Intent(this, UserActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, UserOrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_profile;
        });
    }

    private void cargarDatosPerfil() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getPerfil(idUsuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario u = response.body();
                    if (u.getDireccion() != null) etDireccion.setText(u.getDireccion());
                    if (u.getNotas() != null) etNotas.setText(u.getNotas());
                    if (u.getPreferencias() != null) etPreferencias.setText(u.getPreferencias());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("PROFILE_ERROR", "Error cargando: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap scaledBitmap = redimensionarBitmap(bitmap, 400);
                ivProfile.setImageBitmap(scaledBitmap);
                encodedImage = encodeImage(scaledBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap redimensionarBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void guardarCambios() {
        String dir = etDireccion.getText().toString();
        String notas = etNotas.getText().toString();
        String pref = etPreferencias.getText().toString();

        Usuario u = new Usuario();
        u.setDireccion(dir);
        u.setNotas(notas);
        u.setPreferencias(pref);
        u.setImagen(encodedImage.isEmpty() ? null : encodedImage);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.actualizarPerfil(idUsuario, u).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
