package com.example.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class PresentScreen2Activity extends AppCompatActivity {

    // 1. Define la duración de la pantalla en milisegundos (2000 ms = 2 segundos)
    private static final long SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_screen_2); // Muestra el layout con tu logo

        // 2. Crea un Handler para ejecutar una tarea después del tiempo definido
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 3. Esta parte del código se ejecutará después de 2 segundos

                // Crea el Intent para ir a LoginActivity
                Intent intent = new Intent(PresentScreen2Activity.this, LoginActivity.class);
                startActivity(intent);

                // 4. Cierra todas las actividades anteriores del flujo de presentación
                // para que el usuario no pueda volver a ellas con el botón "atrás".
                finishAffinity();
            }
        }, SPLASH_TIME_OUT);
    }
}
