ALTER TABLE master_authentication.user_profile
DROP CONSTRAINT city_id_fk;

ALTER TABLE master_authentication.user_profile
DROP COLUMN city_id;

DROP SCHEMA master_address CASCADE;