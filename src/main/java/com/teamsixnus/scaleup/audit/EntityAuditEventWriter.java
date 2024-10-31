package com.teamsixnus.scaleup.audit;

import com.teamsixnus.scaleup.domain.enumeration.EntityAuditAction;

@FunctionalInterface
public interface EntityAuditEventWriter {
    public void writeAuditEvent(Object target, EntityAuditAction action);
}
