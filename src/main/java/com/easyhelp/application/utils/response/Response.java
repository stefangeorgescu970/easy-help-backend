package com.easyhelp.application.utils.response;

import lombok.*;

import java.io.Serializable;

@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Response implements Serializable {

    /**
     * Variable of type {@link Boolean} representing the status of the
     *      operation for which the response is given.
     */
    protected boolean status;

    /**
     * Method for a response builder.
     * @return An object of type {@link Response.Builder}
     *      which in turn is used to build a {@link Response} object.
     */
    static Builder builder() {
        return new Builder();
    }


    /**
     * Response object builder class.
     */
    static protected class Builder {

        private boolean status;

        public Builder status(boolean value) {
            this.status = value;
            return this;
        }

        public Response build() {
            return new Response(this.status);
        }
    }
}


