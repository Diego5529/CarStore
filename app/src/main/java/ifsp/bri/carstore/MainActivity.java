package ifsp.bri.carstore;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.content.DialogInterface;

import java.security.MessageDigest;

import static java.lang.System.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int tipo = 0;
    private int tipo2 = 5;
    private double totalLiquido = 0;
    private double totalBruto = 0;
    private double totalDescontos = 0;
    private int percDesconto = 0;
    private int dias = 0;
    private Boolean arCondicionado = false;
    private Boolean airBag = false;
    private Boolean Freio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewBruto = (TextView) findViewById(R.id.textViewTotalBruto);
        textViewBruto.setText("");

        TextView textViewDesconto = (TextView) findViewById(R.id.textViewTotalDescontos);
        textViewDesconto.setText("");

        TextView textViewLiquido = (TextView) findViewById(R.id.textViewTotalLiquido);
        textViewLiquido.setText("");

        Log.d(TAG, ">>>>>>>onCreate()<<<<<<<<<");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_popular:
                if (checked)
                    tipo = 0;
                    break;
            case R.id.radio_medio:
                if (checked)
                    tipo = 1;
                    break;
            case R.id.radio_grande:
                if (checked)
                    tipo = 2;
                    break;
        }

        Log.d(TAG, "Tipo: " + tipo);
    }

    public void onRadioButton2Clicked(View view) {
        // Is the button now checked?

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_somPadrao:
                if (R.id.radio_somPadrao == tipo2)
                    ((RadioButton) view).setChecked(false);
                else
                    ((RadioButton) view).setChecked(true);

                if (((RadioButton) view).isChecked()) {
                    tipo2 = 2131492950;
                }else{
                    tipo2 = 0;
                }
                break;
            case R.id.radio_somEspecial:
                if (R.id.radio_somEspecial == tipo2)
                    ((RadioButton) view).setChecked(false);
                else
                    ((RadioButton) view).setChecked(true);

                if (((RadioButton) view).isChecked()) {
                    tipo2 = 2131492951;
                }else{
                    tipo2 = 0;
                }

               break;
        }

        Log.d(TAG, "Tipo2: " + tipo2 + "Radio Padrao: " + R.id.radio_somPadrao + "Radio Especial: " + R.id.radio_somEspecial );
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_arCondicionado:
                arCondicionado = checked ? true : false;
                break;
            case R.id.checkbox_airBag:
                airBag = checked ? true : false;
                break;
            case R.id.checkbox_freioABS:
                Freio = checked ? true : false;
                break;
        }
    }

    public void onClickButtonCalcular(View view){
        //Dias
        EditText editText = (EditText) findViewById(R.id.edit_dias);
        if (editText.getText().length() > 0) {
            String diasString = editText.getText().toString();
            if (diasString != null) {
                dias = Integer.parseInt(diasString);
                Log.d(TAG, "Dias: " + dias);
            }
        }

        if (dias > 0) {
            calculaTotal();
        }
        else
        {
            String titleMessage = getResources().getString(R.string.titleMessage);
            String messageError = getResources().getString(R.string.messageError);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(titleMessage);
            alertDialog.setMessage(messageError);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void calculaTotal(){
        //Total Tipo
        int totalTipo = 0;
        switch (tipo){
            case 0:
                totalTipo = 55;
                break;
            case 1:
                totalTipo = 85;
                break;
            case 2:
                totalTipo = 110;
                break;
        }

        Log.d(TAG, "Total Tipo: " + totalTipo);

        //Opcionais
        int totalOpcionais = 0;
        if (arCondicionado)
            totalOpcionais += 25;
        if (airBag)
            totalOpcionais += 20;
        if (Freio)
            totalOpcionais += 15;

        switch (tipo2) {
            case 2131492950:
                totalOpcionais += 10;
                break;
            case 2131492951:
                totalOpcionais += 30;
                break;
        }
        Log.d(TAG, "Total Opcionais: " + totalOpcionais);

        //Totalizar
        int totalSelecionado = (totalTipo + totalOpcionais) * dias;
        Log.d(TAG, "Total Selecionado: " + totalSelecionado);

        //Resultados
        totalLiquido = calculaDescontos(totalSelecionado);

        Resources res = getResources();
        String textTotalBruto = String.format(res.getString(R.string.total_bruto),
                totalBruto);
        TextView textViewBruto = (TextView) findViewById(R.id.textViewTotalBruto);
        textViewBruto.setText(textTotalBruto);

        String textTotalDescontos = String.format(res.getString(R.string.total_descontos),
                totalDescontos, percDesconto);
        TextView textViewDesconto = (TextView) findViewById(R.id.textViewTotalDescontos);
        textViewDesconto.setText(textTotalDescontos);

        String textTotalLiquido = String.format(res.getString(R.string.total_liquido),
                totalLiquido);
        TextView textViewLiquido = (TextView) findViewById(R.id.textViewTotalLiquido);
        textViewLiquido.setText(textTotalLiquido);

        //Zerando variavels
        dias = 0;
        totalOpcionais = 0;
        totalSelecionado = 0;
        totalTipo = 0;
        totalDescontos = 0;
        totalBruto = 0;
        totalLiquido = 0;
        percDesconto = 0;
    }

    private double calculaDescontos(double totalSelecionado){
        totalBruto = totalSelecionado;

        if (dias > 0 ){
            if (dias >= 6 && dias <= 10) {
                percDesconto = 10;
                totalDescontos = totalBruto * 0.1;
            }else if (dias > 10) {
                percDesconto = 20;
                totalDescontos = totalBruto * 0.2;
            }
        }

        return totalBruto - totalDescontos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Log.d(TAG, "==========onCreateOptionsMenu()===========");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        Log.d(TAG, "TESTE");

        return super.onOptionsItemSelected(item);
    }
}
