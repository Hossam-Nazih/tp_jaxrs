package org.example.banque.web;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Conserve le code HTTP d'une erreur et place son message dans le corps de la réponse. */
@Provider
public class ErreurHttpMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException erreur) {
        return Response.fromResponse(erreur.getResponse())
                .entity(erreur.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
