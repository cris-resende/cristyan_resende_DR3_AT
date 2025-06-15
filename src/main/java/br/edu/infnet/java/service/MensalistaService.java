package br.edu.infnet.java.service;

import br.edu.infnet.java.model.Mensalista;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MensalistaService {

    private List<Mensalista> mensalistas = new ArrayList<>();

    public MensalistaService() {
        mensalistas.add(new Mensalista("M001", "Alice Silva", "Desenvolvedora", 5000.0));
        mensalistas.add(new Mensalista("M002", "Bruno Costa", "Designer", 4500.0));
    }

    public void adicionarMensalista(Mensalista mensalista) {
        mensalistas.add(mensalista);
    }

    public Optional<Mensalista> buscarMensalistaPorMatricula(String matricula) {
        return mensalistas.stream()
                .filter(m -> m.getMatricula().equalsIgnoreCase(matricula))
                .findFirst();
    }

    public List<Mensalista> listarTodosMensalistas() {
        return new ArrayList<>(mensalistas);
    }
}
