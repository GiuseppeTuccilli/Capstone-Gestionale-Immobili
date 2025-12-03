package giuseppetuccilli.Capstone.Gestionale.Immobili.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ditte")
@NoArgsConstructor
@Getter
public class Ditta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
}
