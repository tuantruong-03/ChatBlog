package simple.blog.backend.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import simple.blog.backend.model.User;
import simple.blog.backend.service.SequenceGeneratorService;

@Component
@RequiredArgsConstructor
public class UserModelListener extends AbstractMongoEventListener<User> {

    private final SequenceGeneratorService sequenceGeneratorService;
    
    @Override
    public void onBeforeConvert(BeforeConvertEvent<User> event) {
        if (event.getSource().getUserId() == null) {
            event.getSource().setUserId((int)sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            
    }
    }
}
