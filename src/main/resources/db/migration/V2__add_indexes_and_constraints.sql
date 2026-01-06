CREATE INDEX idx_notification_user_id ON notification(user_id);
CREATE INDEX idx_notification_created_at_desc ON notification(created_at DESC);
CREATE INDEX idx_notification_category ON notification(category);

CREATE INDEX idx_notification_user_created ON notification(user_id, created_at DESC);

ALTER TABLE notification
ADD CONSTRAINT chk_category
CHECK (category IN ('SPORTS', 'FINANCIAL', 'MOVIES'));

ALTER TABLE notification 
ADD CONSTRAINT chk_type
CHECK (type IN ('EMAIL', 'SMS', 'PUSH'));

ALTER TABLE notification 
ADD CONSTRAINT chk_status
CHECK (status IN ('PENDING', 'SENT', 'FAILED'));
