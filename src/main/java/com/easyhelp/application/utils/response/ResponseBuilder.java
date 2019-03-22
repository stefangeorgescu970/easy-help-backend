package com.easyhelp.application.utils.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;


/**
 * API used to encode data into a JSON serializable format that will be used as a response object.
 */
public class ResponseBuilder {

    /**
     * Builds a response having a status of 'success' (true) and a single object representing the model
     * (extending {@link Serializable} interface).
     *
     * @param httpStatus The HTTP status for the response.
     * @param object     The actual object representing the model.
     * @param <T>        The type of the model object. Must extend {@link Serializable} interface.
     * @return A response containing a single object representing the model.
     */
    public static <T extends Serializable> ResponseEntity<Response> encode
    (HttpStatus httpStatus, T object) {

        return new ResponseEntity<>(ResponseSingle.<T>builderSingle()
                .object(object).build(), httpStatus);
    }

    /**
     * Builds a response having a status of 'success' (true) and a wrapper for a list of model objects
     * (extending {@link Serializable} interface) that also includes
     * some information such as the page, total amount of pages, amount of objects.
     *
     * @param httpStatus The HTTP status for the response.
     * @param objects    The actual {@link List} of objects representing the model.
     * @param page       The number of the current page.
     * @param total      The total amount of pages.
     * @param count      The total amount of objects.
     * @param <T>        The type of the model objects. Must extend {@link Serializable} interface.
     * @return A response containing a list of model objects.
     */
    public static <T extends Serializable> ResponseEntity<Response> encode
    (HttpStatus httpStatus, List<T> objects, int page, int total, int count) {

        return new ResponseEntity<>(ResponseList.<T>builderList()
                .objects(objects).page(page).total(total).count(count).build(), httpStatus);
    }

    /**
     * Builds an empty response.
     *
     * @param httpStatus The HTTP status for the response.
     * @return A response containing absolutely nothing.
     */
    public static ResponseEntity<Response> encode(HttpStatus httpStatus) {
        return new ResponseEntity<>(ResponseEmpty.builderEmpty().build(), httpStatus);
    }

    /**
     * Builds an exception response having a status of 'failure' (false) and a wrapper containing
     * information about the {@link RuntimeException} that occurred (message for now)!
     *
     * @param httpStatus The HTTP status for the response.
     * @param exception  Variable of type {@link RuntimeException} representing an exception that occurred
     *                   during an operation.
     * @return A response representing an exception.
     * @see ResponseBuilder
     */
    public static <T extends RuntimeException> ResponseEntity<Response> encode
    (HttpStatus httpStatus, T exception) {

        return new ResponseEntity<>(ResponseException.builderException()
                .exception(exception).build(), httpStatus);
    }

    /**
     * Builds an exception response having a status of 'failure' (false) and a wrapper containing
     * information about the {@link RuntimeException} that occurred (message for now)!
     *
     * @param httpStatus       The HTTP status for the response.
     * @param exceptionMessage Variable of type {@link String} representing the message of the Exception.
     * @return A response representing an exception.
     * @see ResponseBuilder
     */
    public static ResponseEntity<Response> encode
    (HttpStatus httpStatus, String exceptionMessage) {

        return new ResponseEntity<>(ResponseException.builderException()
                .exception(new RuntimeException(exceptionMessage)).build(), httpStatus);
    }
}

