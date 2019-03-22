package com.easyhelp.application.utils.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ResponseException extends Response {

    /**
     * Variable of type {@link String} representing the message of an
     *      exception that occurred during the operation for which the response is given.
     */
    protected String exception;

    /**
     * Method for a response builder.
     * @return An object of type {@link Response.Builder}
     *      which in turn is used to build a {@link ResponseException} object.
     */
    static Builder builderException() {
        return new Builder();
    }

    /**
     * All arguments constructor for a single object response.
     * @param status The status of the operations.
     * @param message The string representing the message of the exception.
     */
    ResponseException(boolean status, String message) {
        super(status);
        this.exception = message;
    }


    /**
     * Response object builder class.
     */
    static protected class Builder {

        private boolean status;

        // Exception relevant fields:
        private String message;

        public Builder() {
            this.status = false;
            this.message = null;
        }

        public Builder exception(RuntimeException value) {
            this.message = value.getMessage();
            return this;
        }

        public Builder exceptionMessage(String value) {
            this.message = value;
            return this;
        }

        public Response build() {
            return new ResponseException(this.status, this.message);
        }
    }

}