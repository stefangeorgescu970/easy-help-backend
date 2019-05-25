package com.easyhelp.application.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class ResponseSingle<T extends Serializable> extends Response {

    /**
     * Variable of type {@link T} representing the actual model of the
     * operation for which the response is given.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T object;

    /**
     * All arguments constructor for a single object response.
     *
     * @param status The status of the operations.
     * @param object The object representing the model for the response.
     */
    ResponseSingle(boolean status, T object) {
        super(status);
        this.object = object;
    }

    /**
     * Method for a response builder.
     *
     * @return An object of type {@link ResponseSingle.Builder}
     * which in turn is used to build a {@link Response} object.
     */
    static <T extends Serializable> Builder<T> builderSingle() {
        return new Builder<T>();
    }


    /**
     * Response object builder class.
     */
    static protected class Builder<BuildT extends Serializable> {

        private boolean status;
        private BuildT object;

        Builder() {
            this.status = true;
            this.object = null;
        }

        public Builder<BuildT> object(BuildT value) {
            this.object = value;
            return this;
        }

        public Response build() {
            return new ResponseSingle<BuildT>(this.status, this.object);
        }
    }
}
