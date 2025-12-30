---- Customers (assuming you have a Customer entity with id + name)
--INSERT INTO customer (id, name) VALUES (1, 'Alice Johnson');
--INSERT INTO customer (id, name) VALUES (2, 'Bob Smith');
--
---- Loan Applications
--INSERT INTO loan_application (
--    application_reference_code,
--    principal,
--    term_months,
--    purpose,
--    customer_annual_income,
--    loan_application_status,
--    reviewed_by,
--    reviewed_at,
--    comments,
--    created_at,
--    updated_at,
--    customer_id
--) VALUES
--    ('APP-10001', 5000.00, 12, 'Car Loan', 45000.00, 'APPROVED', 'Manager1', CURRENT_TIMESTAMP, 'Looks good', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
--    ('APP-10002', 7500.00, 24, 'Home Renovation', 60000.00, 'REJECTED', 'Manager2', CURRENT_TIMESTAMP, 'Insufficient income', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);
--
---- Loans
--INSERT INTO loan (
--    loan_reference_code,
--    principal,
--    type,
--    loan_payment_status,
--    disbursement_date,
--    disbursement_amount,
--    customer_id,
--    loan_application_id
--) VALUES
--    ('LN-10001', 5000.00, 'PERSONAL', 'PENDING', CURRENT_TIMESTAMP, 5000.00, 1, 1),
--    ('LN-10002', 7500.00, 'HOME', 'OVERDUE', CURRENT_TIMESTAMP, 7500.00, 2, 2);
--
---- Repayment Schedules
--INSERT INTO repayment_schedule (loan_id) VALUES (1);
--INSERT INTO repayment_schedule (loan_id) VALUES (2);
--
---- Repayment Schedule Entries (ElementCollection table)
---- Assuming Hibernate names it "repayment_schedule_entries" with columns:
---- repayment_schedule_id, entry_due_date, due_date, principal, interest, interest_payment_amount, principal_payment_amount, total_payment_amount, loan_payment_status
--
--INSERT INTO repayment_schedule_entries (
--    repayment_schedule_id,
--    entry_due_date,
--    due_date,
--    principal,
--    interest,
--    interest_payment_amount,
--    principal_payment_amount,
--    total_payment_amount,
--    loan_payment_status
--) VALUES
--    (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000.00, 200.00, 200.00, 1000.00, 1200.00, 'PENDING'),
--    (1, CURRENT_TIMESTAMP + 30, CURRENT_TIMESTAMP + 30, 1000.00, 180.00, 180.00, 1000.00, 1180.00, 'PENDING'),
--    (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1500.00, 300.00, 300.00, 1500.00, 1800.00, 'OVERDUE');

INSERT INTO customer (id, name, email, mobile_number) VALUES
(1, 'Alice Johnson', 'bijan85426@icousd.com', '5551111'),
(2, 'Bob Smith', 'bijan85426@icousd.com', '5552222'),
(3, 'Charlie Davis', 'bijan85426@icousd.com', '5553333'),
(4, 'Diana Prince', 'bijan85426@icousd.com', '5554444'),
(5, 'Ethan Hunt', 'bijan85426@icousd.com', '5555555');


INSERT INTO loan_application (
    id, application_reference_code, principal, term_months, purpose,
    customer_annual_income, loan_application_status, reviewed_by,
    reviewed_at, comments, created_at, updated_at, customer_id
) VALUES
(1, 'APP-10001', 5000.00, 12, 'Car Loan', 45000.00, 'APPROVED', 'Manager1', CURRENT_TIMESTAMP, 'Approved cleanly', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
(2, 'APP-10002', 7500.00, 24, 'Home Renovation', 60000.00, 'REJECTED', 'Manager2', CURRENT_TIMESTAMP, 'Insufficient income', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
(3, 'APP-10003', 12000.00, 36, 'Education', 55000.00, 'UNDER_REVIEW', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
(4, 'APP-10004', 3000.00, 6, 'Emergency', 30000.00, 'SUBMITTED', 'Manager3', CURRENT_TIMESTAMP, 'Submitted for review', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4),
(5, 'APP-10005', 9500.00, 18, 'Business', 80000.00, 'NOT_SUBMITTED', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);


INSERT INTO loan (
    id, loan_reference_code, principal, type, loan_payment_status,
    disbursement_date, disbursement_amount, customer_id, loan_application_id
) VALUES
(1, 'LN-10001', 5000.00, 'PERSONAL', 'PENDING', CURRENT_TIMESTAMP, 5000.00, 1, 1),
(2, 'LN-10002', 7500.00, 'HOME', 'OVERDUE', CURRENT_TIMESTAMP, 7500.00, 2, 2),
(3, 'LN-10003', 12000.00, 'EDUCATION', 'PRE_DISBURSEMENT', CURRENT_TIMESTAMP, 0.00, 3, 3),
(4, 'LN-10004', 3000.00, 'EMERGENCY', 'PAID', CURRENT_TIMESTAMP, 3000.00, 4, 4),
(5, 'LN-10005', 9500.00, 'BUSINESS', 'NOT_AVAILABLE', CURRENT_TIMESTAMP, 9500.00, 5, 5);


INSERT INTO repayment_schedule (id, loan_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);


-- Loan 1 (PENDING)
INSERT INTO repayment_schedule_entries (
    repayment_schedule_id, entry_due_date, due_date, principal, interest,
    interest_payment_amount, principal_payment_amount, total_payment_amount,
    loan_payment_status
) VALUES
(1, '2025-01-01T00:00:00', '2025-01-01T00:00:00', 1000.00, 200.00, 200.00, 1000.00, 1200.00, 'PENDING'),
(1, '2025-02-01T00:00:00', '2025-02-01T00:00:00', 1000.00, 180.00, 180.00, 1000.00, 1180.00, 'PENDING');

-- Loan 2 (OVERDUE)
INSERT INTO repayment_schedule_entries (
    repayment_schedule_id, entry_due_date, due_date, principal, interest,
    interest_payment_amount, principal_payment_amount, total_payment_amount,
    loan_payment_status
) VALUES
(2, '2025-01-01T00:00:00', '2025-01-01T00:00:00', 1500.00, 300.00, 300.00, 1500.00, 1800.00, 'OVERDUE'),
(2, '2025-02-01T00:00:00', '2025-02-01T00:00:00', 1500.00, 280.00, 280.00, 1500.00, 1780.00, 'OVERDUE');

-- Loan 3 (PRE_DISBURSEMENT)
INSERT INTO repayment_schedule_entries (
    repayment_schedule_id, entry_due_date, due_date, principal, interest,
    interest_payment_amount, principal_payment_amount, total_payment_amount,
    loan_payment_status
) VALUES
(3, '2025-03-01T00:00:00', '2025-03-01T00:00:00', 2000.00, 400.00, 400.00, 2000.00, 2400.00, 'PRE_DISBURSEMENT');

-- Loan 4 (PAID)
INSERT INTO repayment_schedule_entries (
    repayment_schedule_id, entry_due_date, due_date, principal, interest,
    interest_payment_amount, principal_payment_amount, total_payment_amount,
    loan_payment_status
) VALUES
(4, '2024-12-01T00:00:00', '2024-12-01T00:00:00', 3000.00, 0.00, 0.00, 3000.00, 3000.00, 'PAID');

-- Loan 5 (NOT_AVAILABLE)
INSERT INTO repayment_schedule_entries (
    repayment_schedule_id, entry_due_date, due_date, principal, interest,
    interest_payment_amount, principal_payment_amount, total_payment_amount,
    loan_payment_status
) VALUES
(5, '2025-01-15T00:00:00', '2025-01-15T00:00:00', 2000.00, 250.00, 250.00, 2000.00, 2250.00, 'NOT_AVAILABLE');

ALTER TABLE customer ALTER COLUMN id RESTART WITH 6;
ALTER TABLE loan_application ALTER COLUMN id RESTART WITH 6;
ALTER TABLE loan ALTER COLUMN id RESTART WITH 6;
ALTER TABLE repayment_schedule ALTER COLUMN id RESTART WITH 6;
