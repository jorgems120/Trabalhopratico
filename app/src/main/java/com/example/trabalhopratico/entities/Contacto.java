package com.example.trabalhopratico.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Contacto implements Parcelable {
    public String nome;
    public String numero;
    public String idade;
    public String email;
    public String profissao;
    public String localidade;
    public String codpostal;

    public Contacto(String nome, String numero, String idade,
                    String email, String profissao, String localidade, String codpostal){

        this.nome= nome;
        this.numero=numero;
        this.idade=idade;
        this.email=email;
        this.profissao=profissao;
        this.localidade=localidade;
        this.codpostal=codpostal;


    }


    protected Contacto(Parcel in) {
        nome = in.readString();
        numero = in.readString();
        idade = in.readString();
        email = in.readString();
        profissao = in.readString();
        localidade = in.readString();
        codpostal = in.readString();
    }

    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

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

    public String getLocalidade() { return localidade;}

    public String getCodpostal() {
        return codpostal;
    }

    public void setNome(String nome){this.nome = nome;}

    public void setNumero (String numero){this.numero = numero;}

    public void setIdade(String idade){this.idade = idade;}

    public void setEmail(String email){this.email = email;}

    public void setProfissao(String profissao){this.profissao = profissao;}

    public void setLocalidade(String localidade){this.localidade = localidade;}

    public void setCodpostal(String codpostal){this.codpostal = codpostal;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(numero);
        dest.writeString(idade);
        dest.writeString(email);
        dest.writeString(profissao);
        dest.writeString(localidade);
        dest.writeString(codpostal);
    }
}
