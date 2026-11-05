-- liquibase formatted sql
-- changeset unittree-notification:update_db_unittree_notification-2.0.1-2.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE unittree_unit_notification MODIFY email VARCHAR(1000) DEFAULT '' NOT NULL;
