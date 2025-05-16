@Entity
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @OneToMany(mappedBy = "comprador")
    private List<Transacao> transacoes;

    // Getters, Setters, Construtores
}
