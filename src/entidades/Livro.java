package entidades;

import java.util.ArrayList;

public class Livro {

    private final String titulo;
    private final String ISBN;
    private int cod;
    private String descricao;
    
    private ArrayList<Autor> autorList = new ArrayList<>();
    private ArrayList<Capitulo> capitulosList = new ArrayList<>();

    public Livro(String titulo, String ISBN) {
        this.titulo = titulo;
        this.ISBN = ISBN;
    }

    public Livro(String titulo, String ISBN, int cod) {
        this.titulo = titulo;
        this.ISBN = ISBN;
        this.cod = cod;
    }



    public int adicionarCapitulo(String titulo, String texto) {
        if (capitulosList.size() >= 100) {
            return -1;
        }
        try {
            Capitulo capitulo = new Capitulo(titulo, texto);
            capitulosList.add(capitulo);
            return capitulosList.lastIndexOf(capitulo);

        } catch (Exception e) {
            return -1;
        }
    }

    public int removerCapitulo(Capitulo capitulo) {
        int x = 0;
        if (capitulosList.contains(capitulo)) {
            for (int i = 0; i < capitulosList.size(); i++) {
                if (capitulosList.get(i).equals(capitulo)) {
                    capitulosList.remove(i);
                    x = i;
                }
            }
            return x;
        } else {
            return -1;
        }
    }

    public int adicionarAutor(Autor autor) {
        if (autorList.size() >= 6) {
            return -1;
        }
        try {
            for (int i = 0; i < autorList.size(); i++) {
                if (autorList.get(i).getNome().equals(autor.getNome())) {
                    return -1;
                }
            }
            autorList.add(autor);
            return autorList.lastIndexOf(autor);

        } catch (Exception e) {
            return -1;
        }
    }

    public int removerAutor(Autor autor) {
        int x = 0;
        if (autorList.contains(autor)) {
            for (int i = 0; i < autorList.size(); i++) {
                if (autorList.get(i).equals(autor)) {
                    autorList.remove(i);
                    x = i;
                }
            }
            return x;
        } else {
            return -1;
        }
    }

    public ArrayList<Autor> getAutorList() {
        return autorList;
    }
    
    public ArrayList<Capitulo> getCapitulosList() {
        return capitulosList;
    }
    
    @Override
    public String toString() {
        String txt = null;
        for (int i = 0; i < autorList.size(); i++) {
            if (i == 0) {
                txt = autorList.get(i).getNome();
            } else {
                txt += "; " + autorList.get(i).getNome();
            }
        }
        return txt;
    }
    
    public int getCod() {
        return cod;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
