package br.senai.sc.livros.model.factory;

import br.senai.sc.livros.model.entities.Status;

public class StatusFactory {
    public Status getStatus(){
        return Status.AGUARDANDO_REVISAO;
    }
}
