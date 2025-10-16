package org.example.io;

import org.example.model.Event;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Responsável por formatar os dados e escrever os arquivos de saída.
 * (SRP: Formatação e Escrita de I/O)
 */
public class EventWriter {

    private static final String OUTPUT_HEADER = "EVENT|UID|PID|PROCESS_NAME|COUNTER|FIRST_TIMESTAMP";
    private final String basePath;

    public EventWriter(String basePath) {
        this.basePath = basePath;
    }

    public void writeSummarizedFiles(Map<String, Map<Integer, List<Event>>> groupedEvents) throws IOException {
        Files.createDirectories(Paths.get(basePath));

        for (String process : groupedEvents.keySet()) {
            Map<Integer, List<Event>> eventMap = new TreeMap<>(groupedEvents.get(process));

            String safeProcessName = sanitizeProcessName(process);
            Path outputPath = Paths.get(basePath, safeProcessName + ".txt");

            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                writer.write(OUTPUT_HEADER);
                writer.newLine();

                for (Map.Entry<Integer, List<Event>> entry : eventMap.entrySet()) {
                    List<Event> eventList = entry.getValue();
                    writeSummaryLine(writer, entry.getKey(), eventList);
                }
            }
        }
    }

    private static String sanitizeProcessName(String processName) {
        // Substitui caracteres inválidos para nomes de arquivo por '_'
        return processName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private static void writeSummaryLine(BufferedWriter writer, int eventCode, List<Event> eventList) throws IOException {
        // Ordena a lista para garantir que o primeiro timestamp seja o menor (requisito)
        eventList.sort(Comparator.comparingLong(Event::getTimestamp));

        int counter = eventList.size();
        Event firstEvent = eventList.get(0);
        long firstTimestamp = firstEvent.getTimestamp();

        // Escreve a linha (formato limpo)
        writer.write(String.format("%d|%d|%d|%s|%d|%d",
                eventCode,
                firstEvent.getUserId(),
                firstEvent.getProcessId(),
                firstEvent.getProcessName(),
                counter,
                firstTimestamp));
        writer.newLine();
    }
}