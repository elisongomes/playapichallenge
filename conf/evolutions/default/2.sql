# --- !Ups

insert into users (name, email) values ('Pedro Santos', 'pedro.santos@xpto.com');
insert into users (name, email) values ('Cláudio Borges', 'claudio.borges@xpto.com');
insert into users (name, email) values ('Marina Araújo', 'marina.araujo@xpto.com');

insert into leads (name, email) values ('Pedro Menezes', 'pedro.menezes@xpto.com');
insert into lead_phones (phone, lead_id) values ('+5548999999999', 1);
insert into lead_phones (phone, lead_id) values ('+5548999998888', 1);
insert into lead_phones (phone, lead_id) values ('+5548999997777', 1);
insert into lead_status (status, lead_id, user_id, created_at) values ('OPEN', 1, 2, CURRENT_TIMESTAMP);

# --- !Downs

delete from users;