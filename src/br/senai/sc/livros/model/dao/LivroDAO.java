package br.senai.sc.livros.model.dao;

import br.senai.sc.livros.model.entities.*;
import br.senai.sc.livros.model.factory.ConexaoFactory;
import br.senai.sc.livros.model.factory.LivroFactory;
import br.senai.sc.livros.model.factory.PessoaFactory;
import br.senai.sc.livros.model.factory.StatusFactory;

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
                    resultSet.getString("AUTOR_cpf"));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair objeto");
        }
    }

    public void atualizarStatus(int isbn, Status status) {
        String sqlCommand = "update livros set status = " + status.ordinal() + "where isbn = " + isbn;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            try {
                preparedStatement.execute();
            } catch (Exception e) {
                throw new RuntimeException("Erro na execução do comando SQL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na preparação do comando SQL");
        }
    }

    public void atualizarRevisor(int isbn, Revisor revisor) {
        String sqlCommand = "update livros set REVISOR_cpf = " + revisor.getCPF() + "where isbn = " + isbn;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            try {
                preparedStatement.execute();
            } catch (Exception e) {
                throw new RuntimeException("Erro na execução do comando SQL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na preparação do comando SQL");
        }
    }

    public Collection<Livro> getAllLivros() {
        String sqlCommand = "select * from livros";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet != null && resultSet.next()) {
                    Livro livro = new Livro(
                            (Autor) new PessoaDAO().selecionarPorCPF(resultSet.getString("AUTOR_cpf")),
                            resultSet.getString("titulo"),
                            new StatusFactory().getStatus(),
                            resultSet.getInt("qtdPaginas"),
                            resultSet.getInt("isbn"));
                    listaLivros.add(livro);
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro na execução do comando SQL");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro na preparação do comando SQL");
        }
//        return Collections.unmodifiableCollection(listaLivros);
    }

//    public Collection<Livro> selecionarPorAutor(Pessoa pessoa) {
//        Collection<Livro> livrosAutor = new ArrayList<>();
//        for (Livro livro : getAllLivros()) {
//            if (livro.getAutor().equals(pessoa)) {
//                livrosAutor.add(livro);
//            }
//        }
//        return livrosAutor;
//    }

//    public Collection<Livro> selecionarPorStatus(Status status) {
//        Collection<Livro> livrosStatus = new ArrayList<>();
//        for (Livro livro : getAllLivros()) {
//            if (livro.getStatus().equals(status)) {
//                livrosStatus.add(livro);
//            }
//        }
//        return livrosStatus;
//    }
//
//    public Collection<Livro> selecionarAtividadesAutor(Pessoa pessoa) {
//        Collection<Livro> livrosAutor = new ArrayList<>();
//        for (Livro livro : getAllLivros()) {
//            if (livro.getAutor() == pessoa && livro.getStatus().equals(Status.AGUARDANDO_EDICAO)) {
//                livrosAutor.add(livro);
//            }
//            ;
//        }
//        return livrosAutor;
//    }
}
