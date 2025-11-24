package br.tec.db.Pessoa.builder;

import java.time.LocalDate;
import java.util.List;

import br.tec.db.Pessoa.dto.EnderecoDto;
import br.tec.db.Pessoa.dto.PessoaRequestDto;
import br.tec.db.Pessoa.dto.PessoaResponseDto;
import br.tec.db.Pessoa.model.Endereco;
import br.tec.db.Pessoa.model.Pessoa;

public class PessoaBuilder {
    private Long id = 1L;
    private String nome = "Joao Pedro";
    private LocalDate dataNascimento = LocalDate.of(1994, 2, 1);
    private String cpf = "012.333.444-00";
    private List<Endereco> enderecos = List.of(
        new Endereco(1L, "Zero Hora", 2020, "Algarve", "Alvorada", "Rio Grande Do Sul", "94858-000", false, null)
    );
    private List<EnderecoDto> enderecosDto = List.of(
        new EnderecoDto("Zero Hora", 2020, "Algarve", "Alvorada", "Rio Grande Do Sul", "94858-000")
    );

    public static PessoaBuilder umUsuario() {
        return new PessoaBuilder();
    }

    // Métodos para customização
    public PessoaBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public PessoaBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public PessoaBuilder comCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public PessoaBuilder comDataNascimento(LocalDate data) {
        this.dataNascimento = data;
        return this;
    }

    public PessoaBuilder semEnderecos() {
        this.enderecos = List.of();
        this.enderecosDto = List.of();
        return this;
    }

    public Pessoa criarPessoa() {
        return new Pessoa(this.id, this.nome, this.dataNascimento, this.cpf, this.enderecos);
    }

    public PessoaRequestDto criarPessoaRequestDto() {
        return new PessoaRequestDto(this.nome, this.cpf, this.dataNascimento, this.enderecosDto);
    }

    public PessoaResponseDto criarPessoaResponseDto() {
        return new PessoaResponseDto(this.id, this.nome, this.cpf, 0, this.dataNascimento, this.enderecosDto);
    }
}
