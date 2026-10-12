ALTER TABLE master_authentication."user"
ALTER COLUMN created_by TYPE character varying,
ALTER COLUMN created_date TYPE timestamp with time zone USING created_date::timestamp with time zone;

ALTER TABLE master_authentication."user"
ALTER COLUMN created_by SET NOT NULL;

ALTER TABLE master_authentication."user"
ALTER COLUMN created_date SET NOT NULL;

ALTER TABLE master_authentication."user"
ADD UNIQUE(username);

ALTER TABLE master_authentication.role
RENAME " modified_by" TO "modified_by";

ALTER TABLE master_authentication.role
RENAME " modified_date" TO "modified_date";
