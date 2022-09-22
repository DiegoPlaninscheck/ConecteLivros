package br.senai.sc.livros.model.factory;

import br.senai.sc.livros.model.entities.Livro;
import br.senai.sc.livros.model.entities.Status;

public class LivroFactory {

    public Livro getLivro(int isbn, String titulo, int status, int qtdPaginas, int autor_cpf) {
        return new Livro(autor_cpf, titulo, new StatusFactory().getStatus(), qtdPaginas, isbn);
    }
}
