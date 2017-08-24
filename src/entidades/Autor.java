package entidades;

import java.time.LocalDate;
import java.util.ArrayList;

public class Autor {

    private String nome;
    private LocalDate dataNasc;
    private int cod;

    private ArrayList<Livro> livrosDoAutor = new ArrayList<>();

    public Autor(String nome) {
        this.nome = nome;
    }

    public Autor(String nome, LocalDate dataNasc) {
        this.nome = nome;
        this.dataNasc = dataNasc;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public ArrayList<Livro> getLivrosDoAutor() {
        return livrosDoAutor;
    }

}
