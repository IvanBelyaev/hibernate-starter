package org.example.hibernate.listener;

import org.example.hibernate.entity.Audit;
import org.example.hibernate.entity.Audit.Operation;
import org.hibernate.event.spi.*;

import java.util.Optional;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {
    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Operation.DELETE);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Operation operation) {
        if (event.getEntity().getClass() != Audit.class) {
            var id = Optional.ofNullable(event.getId())
                    .map(it -> ((Number) it).longValue())
                    .orElse(null);
            var audit = Audit.builder()
                    .entityId(id)
                    .entityName(event.getPersister().getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().persist(audit);
        }
    }
}
