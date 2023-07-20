INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e6', 'a34bd938-f939-11ed-be56-0242ac120002', 500.00);

INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e1', 'a34bd938-f939-11ed-be56-0242ac120002', 100.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e2', 'a34bd938-f939-11ed-be56-0242ac120002', 600.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e3', 'a34bd938-f939-11ed-be56-0242ac120002', 200.00, 'DEBIT');

INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e7', 'b2cfef0c-f939-11ed-be56-0242ac120002', 100.00);
INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('910a8d6e-4943-4763-b638-c6cf9fef00e4', 'b2cfef0c-f939-11ed-be56-0242ac120002', 100.00, 'DEBIT');