package org.example.service;

import org.example.model.Event;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe de Serviço responsável pela lógica de processamento dos eventos.
 * (SRP: Lógica de Negócio/Agrupamento)
 */
public class EventService {

    /**
     * Agrupa eventos primeiro por nome de processo e depois por código de evento.
     */
    public Map<String, Map<Integer, List<Event>>> groupEventsByProcessAndCode(List<Event> events) {
        return events.stream()
                .collect(Collectors.groupingBy(
                        Event::getProcessName,
                        Collectors.groupingBy(Event::getEventCode)
                ));
    }
}