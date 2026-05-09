package activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import itson.edu.rentamanteleria.R;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        String nombre = getIntent().getStringExtra("nombre");
        TextView tv = findViewById(R.id.tvBienvenidaAdmin);
        tv.setText("Bienvenido Admin, " + nombre);
    }
}
