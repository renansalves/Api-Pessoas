
package br.tec.db.Pessoa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @NotBlank
	private String nome;

  @NotNull
	private LocalDate dataNascimento;

	@Column(nullable = false, unique = true)
	private String cpf;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "pessoa_id")
	private List<Endereco> enderecos = new ArrayList<Endereco>();

  public void validarEDefinirEnderecoPrincipal(){

    long principaisCount = this.enderecos.stream()
        .filter(Endereco::isPrincipal)
        .count();

    if (principaisCount == 0 && !this.enderecos.isEmpty()) {
        // Se não houver nenhum, define o primeiro como principal
        this.enderecos.get(0).setPrincipal(true);
        return;
    }
    
    if (principaisCount > 1) {
        boolean firstFound = false;
        for (Endereco endereco : this.enderecos) {
            if (endereco.isPrincipal) {
                if (!firstFound) {
                    firstFound = true; // Mantém este como principal
                } else {
                    endereco.setPrincipal(false); // Desmarca os restantes
                }
            }
        }
    }
  }
  

}
