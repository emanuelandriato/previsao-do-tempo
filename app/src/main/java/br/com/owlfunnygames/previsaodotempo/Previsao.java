package br.com.owlfunnygames.previsaodotempo;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alexandre on 06/08/16.
 */

public class Previsao {

    String periodo;
    String temperatura;
    String icone;
    String descricao;

    //todo criar classe Localizacao que será extendida de previsao para usuario escolher a cidade
    String cidade;
    String estado;

    final static SimpleDateFormat dateFormat =
                   new SimpleDateFormat("dd, EEEE");


    public Previsao() {
    }

    public Previsao(String temperatura, String periodo) {
        this.temperatura = temperatura;
        this.periodo = periodo;
        this.estado = "CE";
        this.cidade = "Fortaleza";
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(long periodo) {
        this.periodo = dateFormat.format(new Date(periodo));
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
