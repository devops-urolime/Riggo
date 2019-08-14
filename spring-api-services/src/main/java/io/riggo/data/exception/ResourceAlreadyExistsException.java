package io.riggo.data.exception;

import io.riggo.data.domain.ResourceType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Resource Already Exists")
public class ResourceAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -3332292346834265371L;

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(ResourceType resourceType, long id) {
        super("The resource already exists type=" + resourceType.getDisplayName() + " with id=" + id);
    }

    public ResourceAlreadyExistsException(ResourceType resourceType, String extSysId) {
        super("The resource already exists type type=" + resourceType.getDisplayName() + " with extSysId=" + extSysId);
    }
}