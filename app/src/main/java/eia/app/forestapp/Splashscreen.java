package eia.app.forestapp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splashscreen extends Activity {

      //** SET DE LA PANTALLA E IMAGEN QUE SE VA MOSTRAR**//
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashThread; //Declaración de proceso en segundo plano
    @Override

//** FUNCIÓN CENTRAL **//
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen); // carga la imagen central insitucional
        StartAnimations(); //invoca la Función de animación
    }

    //** FUNCIÓN INICIO ANIMACIÓN **//
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha); // carga la animación ubicada en res > anim
        anim.reset(); // esta animación hace cambio de color de negro a blanco
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay); // ubica el layout desde xml de splashscreen
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate); // carga el traslado de la imagen
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash); // carga imagen que se va trasladar ubicada  en res > drawable
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // definición de tiempos de la inimación
                    while (waited < 3000) {
                        sleep(80);
                        waited += 100;
                    }
                    // al final de la animación se lanza la Actividad MAINACTIVITY
                    Intent intent = new Intent(Splashscreen.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
                } catch (InterruptedException e) {

                } finally {
                    Splashscreen.this.finish();
                }

            }
        };
        splashThread.start();

    }

}