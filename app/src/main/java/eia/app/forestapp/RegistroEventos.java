package eia.app.forestapp;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegistroEventos extends AppCompatActivity implements OnClickListener {


    String NombreEvento;
    String DateTime;
    String CantidadEvento;


    EditText txtObservacion;
    int cant;
    TextView txtCantidad;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_eventos);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final LinearLayout fragment1=(LinearLayout)findViewById(R.id.registroeventos);

        //OBSERVACION//
        txtObservacion =(EditText)findViewById(R.id.editobservacion);


        fragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment1.requestFocus(); // use this to trigger the focus listner
                //or use code below to set the keyboard to hidden
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fragment1.getWindowToken(), 0);

            }
        });
        txtObservacion.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtObservacion, InputMethodManager.SHOW_IMPLICIT);
                }
                else
                {
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtObservacion.getWindowToken(), 0);
                }
            }
        });
// ACTION BAR modificacion//
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_regreso);
        TextView toolbar=(TextView)findViewById(R.id.mytext);
        toolbar.setText("Registrar Evento");
        ImageView regresar=(ImageView)findViewById(R.id.regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //------------------------//

        //SELECCION DE EVENTO //
        final Button clickButton = (Button) findViewById(R.id.tipoevento);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupmenu = new PopupMenu(RegistroEventos.this, clickButton);
                popupmenu.getMenuInflater().inflate(R.menu.popoutmenu, popupmenu.getMenu());

                popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.abono:
                                clickButton.setText("Abono");
                                NombreEvento="Abono";
                                break;
                            case R.id.sembrar:
                                clickButton.setText("Siembra");
                                NombreEvento="Siembra";
                                break;
                            case R.id.recolecta:
                                clickButton.setText("Recolección");
                                NombreEvento="Recolecta";
                                break;
                            default:
                                break;


                        }
                        return true;
                    }
                });
                popupmenu.show();

            }


        });

        //FECHA EVENTO//


        final Calendar myCalendar = Calendar.getInstance();

        final Button edittext= (Button) findViewById(R.id.fechaevento);
       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                DateTime=sdf.format(myCalendar.getTime());
                edittext.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistroEventos.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // CANTIDAD DE EVENTO //

        ImageView mas=(ImageView)findViewById(R.id.mas);
        mas.setOnClickListener(this);
        ImageView menos=(ImageView)findViewById(R.id.menos);
        menos.setOnClickListener(this);
        txtCantidad =(TextView)findViewById(R.id.cantidad);
        cant= Integer.parseInt(txtCantidad.getText().toString());

        // BOTON ENVIAR//
        ImageView ingresar=(ImageView)findViewById(R.id.ingresar);
        ingresar.setOnClickListener(this);

        }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mas:
                cant=cant+1;
                txtCantidad.setText(String.valueOf(cant));
                CantidadEvento=String.valueOf(cant);
                break;
            case R.id.menos:
                if(cant != 0){
                    cant=cant-1;
                    txtCantidad.setText(String.valueOf(cant));
                    CantidadEvento=String.valueOf(cant);
                }
                break;
            case R.id.ingresar:
                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String resultado=enviarDatosGET(NombreEvento,DateTime,CantidadEvento,txtObservacion.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(Integer.parseInt(resultado)== 1){
                                    Toast.makeText(getApplicationContext(),"Evento Ingresado",Toast.LENGTH_SHORT).show();

                                } else{
                                    Toast.makeText(getApplicationContext(),"Datos Incompletos",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }

                };
                tr.start();
                AlertDialog.Builder dialog= new AlertDialog.Builder(this);
                dialog.setTitle("Evento Ingresado");
                dialog.setMessage("Su evento "  + NombreEvento+" ha sido registrado con éxito");
                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Main2Activity.class); //abre Actividad de Detalle

                        startActivity(intent);
                    }
                });








                dialog.show();
                break;
            default:
                break;



        }


    }
    public String enviarDatosGET(String nombreEvento, String dateTime, String cantidadEvento, String observacion){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;

        try{

            //OJO CAMBIAR SIEMPRE  POR LA DE IPCONFIG//
            url=new  URL("http://192.168.0.14:81/registro.php?NombreEvento="+nombreEvento+"&FechaEvento="+dateTime+"&CantidadEvento="+cantidadEvento+"&ObservacionEvento="+observacion);
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



}




