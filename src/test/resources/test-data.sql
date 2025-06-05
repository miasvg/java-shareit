
INSERT INTO users (id, name, email) VALUES
                                        (1, 'Owner', 'owner@mail.com'),
                                        (2, 'Booker', 'booker@mail.com');

INSERT INTO items (id, name, description, available, owner_id) VALUES
                                                                   (1, 'Дрель', 'Аккумуляторная', true, 1),
                                                                   (2, 'Отвертка', 'Крестовая', false, 1);