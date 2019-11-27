package com.example.trabalhopratico.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Contacto implements Serializable {
    private Integer id;
    private String nome;
    private String numero;
    private String idade;
    private String email;
    private String profissao;
    private String codpostal;
    private String genero;
    private String localidade_id;

    public Contacto(Integer id, String nome, String numero, String idade,
                    String email, String profissao, String codpostal, String genero, String localidade_id){

        this.id= id;
        this.nome= nome;
        this.numero=numero;
        this.idade=idade;
        this.email=email;
        this.profissao=profissao;
        this.codpostal=codpostal;
        this.genero=genero;
        this.localidade_id=localidade_id;


    }

    public Integer getId() { return id; }

    public String getLocalidade_id() {return localidade_id;}

    public String getnome() {
        return nome;
    }

    public String getnumero() {
        return numero;
    }

    public String getidade() {
        return idade;
    }

    public String getEmail() {
        return email;
    }

    public String getProfissao() {
        return profissao;
    }

    public String getCodpostal() {
        return codpostal;
    }

    public String getGenero() {return genero; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setLocalidade_id(String localidade_id) {this.localidade_id= localidade_id;}

    public void setNome(String nome){this.nome = nome;}

    public void setNumero (String numero){this.numero = numero;}

    public void setIdade(String idade){this.idade = idade;}

    public void setEmail(String email){this.email = email;}

    public void setProfissao(String profissao){this.profissao = profissao;}

    public void setCodpostal(String codpostal){this.codpostal = codpostal;}

}
