package br.senai.sc.livros.model.dao;

import br.senai.sc.livros.model.entities.*;
import br.senai.sc.livros.model.factory.ConexaoFactory;
import br.senai.sc.livros.model.factory.LivroFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class LivroDAO {

    private static Collection<Livro> listaLivros = new HashSet<>();
    private Connection connection;


    public LivroDAO() {
        this.connection = new ConexaoFactory().connectDB();
    }

    public void inserir(Livro livro) {
        String sqlCommand = "insert into livros (isbn, titulo, status, qtdPaginas, AUTOR_cpf) values (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, livro.getISBN());
            preparedStatement.setString(2, livro.getTitulo());
            preparedStatement.setInt(3, livro.getStatus().ordinal());
            preparedStatement.setInt(4, livro.getQntdPaginas());
            preparedStatement.setString(5, livro.getAutor().getCPF());
            try {
                preparedStatement.execute();
            } catch (Exception e) {
                throw new RuntimeException("Erro na execução do comando SQL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na preparação do comando SQL");
        }
    }

    public void remover(Livro livro) {
        listaLivros.remove(livro);
    }

    public Livro selecionar(int isbn) {
        String sqlCommand = "select * from livros where isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, isbn);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extrairObjeto(resultSet);
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro na execução do comando SQL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na preparação do comando SQL");
        }
        throw new RuntimeException("Algo deu ruim");
    }

    private Livro extrairObjeto(ResultSet resultSet) {
        try {
            return new LivroFactory().getLivro(
                    resultSet.getInt("isbn"),
                    resultSet.getString("titulo"),
                    resultSet.getInt("status"),
                    resultSet.getInt("qtdPaginas"),
                    resultSet.getInt("AUTOR_cpf"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair objeto");
        }
    }

    public void atualizar(int isbn, Livro livroAtualizado) {
        for (Livro livro : listaLivros) {
            if (livro.getISBN() == isbn) {
                listaLivros.remove(livro);
                listaLivros.add(livroAtualizado);
            }
            ;
        }

        List<Livro> lista = new ArrayList<>(listaLivros);
        int i = lista.indexOf(selecionar(isbn));
        lista.set(i, livroAtualizado);
        listaLivros.clear();
        listaLivros.addAll(lista);
    }

    public Collection<Livro> getAllLivros() {
        return Collections.unmodifiableCollection(listaLivros);
    }

    ;

    public Collection<Livro> selecionarPorAutor(Pessoa pessoa) {
        Collection<Livro> livrosAutor = new ArrayList<>();
        for (Livro livro : getAllLivros()) {
            if (livro.getAutor().equals(pessoa)) {
                livrosAutor.add(livro);
            }
        }
        return livrosAutor;
    }

    public Collection<Livro> selecionarPorStatus(Status status) {
        Collection<Livro> livrosStatus = new ArrayList<>();
        for (Livro livro : getAllLivros()) {
            if (livro.getStatus().equals(status)) {
                livrosStatus.add(livro);
            }
        }
        return livrosStatus;
    }

    public Collection<Livro> selecionarAtividadesAutor(Pessoa pessoa) {
        Collection<Livro> livrosAutor = new ArrayList<>();
        for (Livro livro : getAllLivros()) {
            if (livro.getAutor() == pessoa && livro.getStatus().equals(Status.AGUARDANDO_EDICAO)) {
                livrosAutor.add(livro);
            }
            ;
        }
        return livrosAutor;
    }


}
