package io.riggo.data.exception;

import io.riggo.data.domain.ResourceType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource Already Exists")
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -3332292346834265371L;

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(ResourceType resourceType, long id) {
        super("The POSTed resource already exists type=" + resourceType.getDisplayName() + " with id=" + id);
    }
}