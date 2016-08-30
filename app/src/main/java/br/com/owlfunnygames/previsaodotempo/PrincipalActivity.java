package br.com.owlfunnygames.previsaodotempo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrincipalActivity extends AppCompatActivity {
    ListView listViewPrevisoes;
    List<Previsao> previsoes = new ArrayList<Previsao>();
    TextView tvTemperaturaHoje;
    TextView tvPeriodoHoje;
    TextView tvCidade;
    ImageView ivIconeHoje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        listViewPrevisoes = (ListView) findViewById(R.id.previsoesListView);
        tvTemperaturaHoje = (TextView) findViewById(R.id.textViewTempHoje);
        tvPeriodoHoje = (TextView) findViewById(R.id.textViewPeriodoHoje);
        tvCidade = (TextView) findViewById(R.id.textViewCidade);
        ivIconeHoje = (ImageView) findViewById(R.id.imageViewIconeHoje);


        PrevisaoAsyncTask p = new PrevisaoAsyncTask();
        p.execute();

        listViewPrevisoes.setAdapter(new ListaPrevisaoAdapter(this, previsoes));

        listViewPrevisoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PrincipalActivity.this, "O item " + position + "foi selecionado",
                        Toast.LENGTH_SHORT).show();
            }
        });

        /* JÁ ESTÁ BUSCANDO PELO GLIDE, PASSOU A SER DESNECESSÁRIO
        DownloadImageTask d = new DownloadImageTask();
        d.execute(Utils.URL_PREVISOES);
        //todo Criar um novo objeto do AsyncTask customizado
        //todo Executar o AsyncTask
        */
    }

    public class DownloadImageTask extends AsyncTask<String, String, Bitmap> {
        private String url;
        private String contexto;
        private ImageView img;

        public DownloadImageTask() {

        }

        public DownloadImageTask(String url){
            this.url = url;
        }

        public DownloadImageTask(String url,ImageView img){
            this.img = img;
            this.contexto = contexto;
            this.url = url;
        }

        @Override
        protected void onPreExecute(){}

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String ... urls){
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            ivIconeHoje.setImageBitmap(result);
        }
    }

    private class PrevisaoAsyncTask extends AsyncTask<String, Void, List<Previsao>> {

        public PrevisaoAsyncTask(){}

        @Override
        protected List<Previsao> doInBackground(String... params) {
            try {
                //Utilizando OkHttp para buscar dados de uma URL
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Utils.URL_PREVISOES)
                        .build();
                Response response = client.newCall(request).execute();
                String responseJson = response.body().string();

                //Trabalhando com JSON MANUALMENTE com apis nativas do Android
                JSONObject jsonObject = new JSONObject(responseJson);
                if (jsonObject != null) {
                    List<Previsao> previsoes = new ArrayList<>();
                    Previsao previsaoTemp;
                    JSONArray listaPrevisoes = jsonObject.getJSONArray("list");

                    for (int i = 1; i < listaPrevisoes.length(); i++) {
                        previsaoTemp = new Previsao();
                        previsaoTemp.setPeriodo(listaPrevisoes.getJSONObject(i).getLong("dt") * 1000);
                        previsaoTemp.setTemperatura(listaPrevisoes.getJSONObject(i).getJSONObject("temp").getDouble("day") + "°");
                        previsaoTemp.setIcone(listaPrevisoes.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                        previsaoTemp.setDescricao(listaPrevisoes.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                        previsoes.add(previsaoTemp);

                        //todo Preencher atributo descricao buscando o dado em weather->description
                        //todo BUG descobrir o que falta aqui
                        //FALTA PERMISSOES NO MANISFESTO E ADICIONAR AS PREVISOES NA LISTA RETORNADA
                    }
                    return previsoes;
                } else {
                    return null;
                }
            } catch (IOException ex) {
                Log.e(PrincipalActivity.class.toString(), "IOException " + ex.getMessage());
                ex.printStackTrace();
            } catch (JSONException ex) {
                Log.e(PrincipalActivity.class.toString(), "JSONException " + ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Previsao> previsoes) {
            //todo checar se vieram previsões
            //todo atualizar previsoes
            //todo exibir Toast caso não tenham vindo previsões
            if(previsoes.size() == 0){
                Toast.makeText(PrincipalActivity.this, "Não foi possível atualizar as previsões.",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                atualizaPrevisoes(previsoes);
            }
        }
    }

    private void atualizaPrevisoes(List<Previsao> previsoes) {
        this.previsoes.clear();
        Previsao previsaoDestaque = previsoes.remove(0);

        //Aqui atualizamos a previsão principal fora do ListView (confira os nomes das variáveis)
        tvTemperaturaHoje.setText(previsaoDestaque.getTemperatura());
        tvPeriodoHoje.setText(previsaoDestaque.getPeriodo());
        tvCidade.setText("Fortaleza, CE");

        //todo Buscar icone do tempo com GLIDE aqui (Após finalizar e testar AsyncTask)
        String urlIcon = String.format(Utils.URL_ICONE,previsaoDestaque.getIcone());
        Glide
                .with(this)
                .load(urlIcon)
                .into(ivIconeHoje);

        this.previsoes.addAll(previsoes);
        ((ArrayAdapter) this.listViewPrevisoes.getAdapter()).notifyDataSetChanged();
    }



}
