INSERT INTO master_authentication."user"(username, password, created_date, created_by, is_delete)
VALUES('tester', '$2a$10$o9Q63Yr.3H6gr3yb8.AYiuCSt1Tk1vVFXzlRl2zQzr4vbl3lPwaPS', NOW(), 'tester', false);

INSERT INTO master_authentication.role(role_name, created_by, created_date, is_delete)
VALUES('SUPER_ADMIN', 'tester', NOW(), false);

INSERT INTO master_authentication.user_role(user_id, role_id, created_by, created_date, is_delete)
VALUES(1, 1, 'tester', NOW(), false);
