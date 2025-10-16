package org.example.model;

/**
 * Classe de Objeto de Transferência de Dados (DTO) para um evento.
 * Campos privados e finais garantem imutabilidade.
 */
public class Event {
    private final long timestamp;
    private final int eventCode;
    private final int userId;
    private final int processId;
    private final String processName;

    public Event(long timestamp, int eventCode, int userId, int processId, String processName) {
        this.timestamp = timestamp;
        this.eventCode = eventCode;
        this.userId = userId;
        this.processId = processId;
        this.processName = processName;
    }

    // Getters para acessar os dados
    public long getTimestamp() {
        return timestamp;
    }

    public int getEventCode() {
        return eventCode;
    }

    public int getUserId() {
        return userId;
    }

    public int getProcessId() {
        return processId;
    }

    public String getProcessName() {
        return processName;
    }
}