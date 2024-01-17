package org.example.hibernate.listener;

import org.example.hibernate.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class MyRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        var revision = (Revision) revisionEntity;
        revision.setUsername("current user");
    }
}
