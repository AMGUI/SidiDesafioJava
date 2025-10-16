package org.example;

import org.example.io.EventReader;
import org.example.io.EventWriter;
import org.example.model.Event;
import org.example.service.EventService;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;

/**
 * Ponto de entrada e orquestrador da aplicação.
 * (SRP: Coordenação da Aplicação)
 */
public class EventProcessor {

    // Constantes de configuração
    private static final String BASE_PATH = "C:\\Users\\Samsung\\Desktop\\Sidi";
    private static final String INPUT_FILENAME = "teste.txt";

    public static void main(String[] args) {

        // Inicializa os componentes (dependências)
        EventReader reader = new EventReader(BASE_PATH, INPUT_FILENAME);
        EventService service = new EventService();
        EventWriter writer = new EventWriter(BASE_PATH);

        try {
            // 1. LEITURA (Delegado ao Reader)
            List<Event> events = reader.readAllEvents();

            if (events.isEmpty()) {
                System.out.println("Nenhum evento válido foi lido do arquivo.");
                return;
            }

            // 2. PROCESSAMENTO (Delegado ao Service)
            Map<String, Map<Integer, List<Event>>> groupedEvents = service.groupEventsByProcessAndCode(events);

            // 3. ESCRITA (Delegado ao Writer)
            writer.writeSummarizedFiles(groupedEvents);

            System.out.println("Arquivos gerados com sucesso na pasta: " + BASE_PATH);

        } catch (NoSuchFileException e) {
            System.err.println("ERRO: O arquivo de entrada não foi encontrado. Caminho procurado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de I/O ao processar arquivos.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}