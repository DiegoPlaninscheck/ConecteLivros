package br.senai.sc.livros.model.service;

import br.senai.sc.livros.model.dao.LivroDAO;
import br.senai.sc.livros.model.entities.*;
import br.senai.sc.livros.view.Menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LivroService {

    public void inserir(Livro livro) {
        new LivroDAO().inserir(livro);
    }

    public void remover(Livro livro) {
        new LivroDAO().remover(livro);
    }

    public Livro selecionar(int isbn) {
        return new LivroDAO().selecionar(isbn);
    }

    public void atualizar(int isbn, Livro livroAtualizado) {
        new LivroDAO().atualizar(isbn, livroAtualizado);
    }

    public Collection<Livro> getAllLivros(){
        Pessoa usuario = Menu.getUsuario();

        if(usuario instanceof Autor){
            return new LivroDAO().selecionarPorAutor(usuario);
        } else if(usuario instanceof Revisor){
            Collection<Livro> livros = new LivroDAO().selecionarPorStatus(Status.AGUARDANDO_REVISAO);
            livros.addAll(new LivroDAO().selecionarPorStatus(Status.EM_REVISAO));
            return livros;
        } else {
            return new LivroDAO().getAllLivros();
        }
    }

    public Collection<Livro> selecionarPorAutor(Pessoa pessoa) {
        return new LivroDAO().selecionarPorAutor(pessoa);
    }

    public Collection<Livro> selecionarPorStatus(Status status) {
        return new LivroDAO().selecionarPorStatus(status);
    }

    public Collection<Livro> selecionarAtividadesAutor(Pessoa pessoa) {
        return new LivroDAO().selecionarAtividadesAutor(pessoa);
    }

    public Collection<Livro> listarAtividades(Pessoa pessoa){
        if(pessoa instanceof Autor){
            return selecionarAtividadesAutor(pessoa);
        } else if(pessoa instanceof Revisor){
            Collection<Livro> livros = new LivroDAO().selecionarPorStatus(Status.AGUARDANDO_REVISAO);
            for(Livro livro : new LivroDAO().selecionarPorStatus(Status.EM_REVISAO)){
                if(livro.getRevisor() == pessoa){
                    livros.add(livro);
                }
            }
            return livros;
        } else {
            return selecionarPorStatus(Status.APROVADO);
        }
    }

    public Livro selecionarPorISBN(int isbn){
        for(Livro livro : getAllLivros()){
            if(livro.getISBN() == isbn){
                return livro;
            }
        }
        throw new RuntimeException("ISBN não encontrado!");
    }

    public void atualizarStatus(Livro livro, Status status){
        livro.setStatus(status);
        atualizar(livro.getISBN(), livro);
    }

    public void adicionarRevisor(Livro livro, Revisor revisor){
        livro.setRevisor(revisor);
        atualizar(livro.getISBN(), livro);
    }
}
