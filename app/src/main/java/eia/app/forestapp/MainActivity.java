package eia.app.forestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnIngreso;

    EditText txtUsu,txtPas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();

        txtUsu=(EditText)findViewById(R.id.nombre);
        txtPas=(EditText)findViewById(R.id.contraseña);
        btnIngreso=(Button)findViewById(R.id.ingresar);
        btnIngreso.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        Thread tr=new Thread(){
            @Override
            public void run() {
                final String resultado=enviarDatosGET(txtUsu.getText().toString(),txtPas.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r=obtDatosJSON(resultado);
                        if(r>0){
                            Intent i =new Intent(getApplicationContext(),Main2Activity.class);
                            i.putExtra("cod",txtUsu.getText().toString());

                            startActivity(i);

                        } else{
                            Toast.makeText(getApplicationContext(),"DATOS INCORRECTOS",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }

        };
        tr.start();
    }

    public String enviarDatosGET(String username,String password){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;

        try{

            //OJO CAMBIAR SIEMPRE  POR LA DE IPCONFIG//
            url=new  URL("http://localhost/app/login.php?usuario="+username+"&contrasena="+password);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();

            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));

                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }

            }



        }catch(Exception e){}
        return resul.toString();


    }


    public int obtDatosJSON(String response){
        int res=0;

        try{
            JSONArray json= new JSONArray(response);
            if(json.length()>0){
                res=1;
            }
        }catch(Exception e){}
        return res;
    }


}