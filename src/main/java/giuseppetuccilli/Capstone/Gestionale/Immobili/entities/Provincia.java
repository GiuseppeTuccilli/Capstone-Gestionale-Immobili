package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "province")
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String nomeProvincia;
    private String sigla;
    private String nomeRegione;


    public Provincia(String nomeProvincia, String sigla, String nomeRegione) {
        this.nomeProvincia = nomeProvincia;
        this.sigla = sigla;
        this.nomeRegione = nomeRegione;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Provincia provincia = (Provincia) o;
        return id == provincia.id && Objects.equals(nomeProvincia, provincia.nomeProvincia) && Objects.equals(sigla, provincia.sigla) && Objects.equals(nomeRegione, provincia.nomeRegione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeProvincia, sigla, nomeRegione);
    }
}
