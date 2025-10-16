package org.example.io;

import org.example.model.Event;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Responsável por ler o arquivo de entrada e fazer o parsing das linhas em objetos Event.
 * (SRP: Leitura e Parsing)
 *
 * Ela lê o conteúdo bruto de um arquivo texto ;
 *
 * Ignora linhas vazias e cabeçalhos;
 *
 * Divide cada linha em partes (campos) usando um delimitador "|";
 *
 * Converte esses campos para tipos corretos (long, int, String);
 *
 * E cria objetos Event a partir desses valores, retornando todos eles em uma lista.
 */
public class EventReader {

    // Constante controladoras
    private static final String FIELD_DELIMITER = "\\|";
    private static final int EXPECTED_FIELDS_COUNT = 5;

    private final String fullInputPath;

    public EventReader(String basePath, String inputFilename) {
        this.fullInputPath = Paths.get(basePath, inputFilename).toString();
    }


    /**
     Método principal: leitura do arquivo
     Exibe qual arquivo vai tentar ler;

     Cria uma lista de Event vazia;

     Usa BufferedReader (recurso seguro com try-with-resources) para abrir o arquivo;

     Lê linha por linha;

     Ignora linhas vazias ou cabeçalhos (TIMESTAMP...);

     Para cada linha válida, chama parseEventLine(line) para criar o objeto Event;

     Se o Optional<Event> vier preenchido, adiciona o evento à lista;

     Retorna a lista final.
     */
    public List<Event> readAllEvents() throws IOException {
        System.out.println("Tentando ler o arquivo: " + fullInputPath);
        List<Event> events = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fullInputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isHeaderOrEmpty(line)) continue;

                // Delega o trabalho de parsing
                Optional<Event> event = parseEventLine(line);
                event.ifPresent(events::add);
            }
        } catch (NoSuchFileException e) {
            // Relança como IOException ou a exceção específica, dependendo do que o caller espera
            throw e;
        }
        return events;
    }

    private static boolean isHeaderOrEmpty(String line) {
        String trimmedLine = line.trim();
        return trimmedLine.isEmpty() || trimmedLine.startsWith("TIMESTAMP");
    }

    private static Optional<Event> parseEventLine(String line) {
        String[] parts = line.split(FIELD_DELIMITER);

        if (parts.length < EXPECTED_FIELDS_COUNT) {
            System.err.println("Aviso: Linha ignorada por número incorreto de campos: " + line);
            return Optional.empty();
        }

        try {
            long timestamp = Long.parseLong(parts[0].trim());
            int eventCode = Integer.parseInt(parts[1].trim());
            int userId = Integer.parseInt(parts[2].trim());
            int processId = Integer.parseInt(parts[3].trim());
            String processName = parts[4].trim();

            return Optional.of(new Event(timestamp, eventCode, userId, processId, processName));
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato de número na linha: " + line);
            return Optional.empty();
        }
    }
}