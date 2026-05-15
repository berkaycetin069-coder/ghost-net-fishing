package de.iu.ghostnet.exception;

public class GhostNetNotFoundException extends RuntimeException {

    public GhostNetNotFoundException(Long ghostNetId) {
        super("Geisternetz mit ID " + ghostNetId + " wurde nicht gefunden.");
    }
}
