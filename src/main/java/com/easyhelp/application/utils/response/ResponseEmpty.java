package com.easyhelp.application.utils.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class ResponseEmpty extends Response {

    private EmptyObjectWrapper object;

    ResponseEmpty(boolean status) {
        super(status);
        this.object = new EmptyObjectWrapper();
    }

    /**
     * Method for a response builder.
     * @return An object of type {@link ResponseEmpty.Builder}
     *      which in turn is used to build a {@link Response} object.
     */
    static <T extends Serializable> Builder<T> builderEmpty() {
        return new Builder<T>();
    }


    /**
     * Response object builder class.
     * NOTE: This is just for completeness.
     */
    static protected class Builder<BuildT extends Serializable> {

        private boolean status;

        Builder() {
            this.status = true;
        }

        public Response build() {
            return new ResponseEmpty(this.status);
        }
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @JsonSerialize
    class EmptyObjectWrapper implements Serializable {

    }
}

