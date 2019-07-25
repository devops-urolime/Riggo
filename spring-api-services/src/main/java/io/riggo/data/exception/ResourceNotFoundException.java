package io.riggo.data.exception;

import io.riggo.data.domain.ResourceType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found") //404
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3332292346834265371L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(ResourceType resourceType, long id) {
        super("ResourceNotFoundException type=" + resourceType.getDisplayName() + " with id=" + id);
    }

    public ResourceNotFoundException(ResourceType resourceType, String extSysId) {
        super("ResourceNotFoundException type=" + resourceType.getDisplayName() + " with extSysId=" + extSysId);
    }
}