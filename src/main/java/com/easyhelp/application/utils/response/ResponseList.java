package com.easyhelp.application.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class ResponseList<T extends Serializable> extends Response {

    /**
     * Variable of type {@link ObjectsListWrapper} representing the actual wrapped model of the
     *      operation for which the response is given.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ObjectsListWrapper object;

    /**
     * All arguments constructor for a list object response.
     * @param status The status of the operation.
     * @param objects The actual list of objects representing the model.
     * @param page The page where the objects was taken from.
     * @param count The count of the objects.
     * @param total The total amount of objects.
     */
    ResponseList(boolean status, List<T> objects, int page, int count, int total) {
        super(status);
        this.object = new ObjectsListWrapper(objects, page, count, total);
    }

    /**
     * Method for a response builder.
     * @return An object of type {@link ResponseList.Builder}
     *      which in turn is used to build a {@link Response} object.
     */
    static <T extends Serializable> Builder<T> builderList() {
        return new Builder<T>();
    }


    /**
     * Response object builder class.
     */
    static protected class Builder<BuildT extends Serializable> {

        private boolean status;
        private List<BuildT> objects;
        private int page;
        private int count;
        private int total;

        Builder() {
            this.status = true;
            this.objects = null;
            this.page = 0;
            this.count = 0;
            this.total = 0;
        }

        public Builder<BuildT> objects(List<BuildT> value) {
            this.objects = value;
            return this;
        }

        public Builder<BuildT> page(int value) {
            this.page = value;
            return this;
        }

        public Builder<BuildT> count(int count) {
            this.count = count;
            return this;
        }

        public Builder<BuildT> total(int total) {
            this.total = total;
            return this;
        }

        public Response build() {
            return new ResponseList<BuildT>(this.status,
                    this.objects, this.page, this.count, this.total);
        }
    }


    /**
     * Response model list wrapper class.
     */
    @Setter(value = AccessLevel.PACKAGE)
    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    private class ObjectsListWrapper implements Serializable {

        /**
         * Variable of type {@link List} containing objects of type {@link T}
         *      representing the actual model of the operation which the response is given to.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<T> objects;


        // Other information, page information.

        private int page;
        private int count;
        private int total;
    }
}

