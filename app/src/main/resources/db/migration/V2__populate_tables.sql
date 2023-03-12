INSERT INTO customer (customer_id, firstname, lastname, gender, dob)
VALUES ('CST0000001', 'Brendan', 'Buckley', 0, DATE '2008-01-01');

INSERT INTO customer_order (customer_id, order_id, total, status, datetime, updated)
VALUES ((SELECT id from customer where customer_id = 'CST0000001'), 'ORD0000001', 20, 0, '19801225 13:00:00+00', null);

INSERT INTO order_line (order_id, quantity, product_id, name, price, category)
VALUES ((SELECT id from customer_order where order_id = 'ORD0000001'), 2, 'PRD0000001', 'Heineken', 10, 0);