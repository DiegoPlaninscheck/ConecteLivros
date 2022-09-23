package br.senai.sc.livros.model.factory;

import br.senai.sc.livros.model.dao.PessoaDAO;
import br.senai.sc.livros.model.entities.Autor;
import br.senai.sc.livros.model.entities.Livro;

public class LivroFactory {

    public Livro getLivro(int isbn, String titulo, int status, int qtdPaginas, String autor_cpf) {
        return new Livro((Autor) new PessoaDAO().selecionarPorCPF(autor_cpf), titulo, new StatusFactory().getStatus(), qtdPaginas, isbn);
    }
}
