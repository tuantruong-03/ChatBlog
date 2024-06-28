package simple.blog.backend.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.model.Role;
import simple.blog.backend.service.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class RoleModelListener extends AbstractMongoEventListener<Role> {

    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Role> event) {
        if (event.getSource().getRoleId() == null) {
            event.getSource().setRoleId((int)sequenceGeneratorService.generateSequence(Role.SEQUENCE_NAME));
            
    }
    }
}
