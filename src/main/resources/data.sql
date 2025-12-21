-- Customers (assuming you have a Customer entity with id + name)
INSERT INTO customer (id, name) VALUES (1, 'Alice Johnson');
INSERT INTO customer (id, name) VALUES (2, 'Bob Smith');

-- Loan Applications
INSERT INTO loan_application (
    application_reference_code,
    principal,
    term_months,
    purpose,
    customer_annual_income,
    loan_application_status,
    reviewed_by,
    reviewed_at,
    comments,
    created_at,
    updated_at,
    customer_id
) VALUES
    ('APP-10001', 5000.00, 12, 'Car Loan', 45000.00, 'APPROVED', 'Manager1', CURRENT_TIMESTAMP, 'Looks good', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
    ('APP-10002', 7500.00, 24, 'Home Renovation', 60000.00, 'REJECTED', 'Manager2', CURRENT_TIMESTAMP, 'Insufficient income', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

-- Loans
INSERT INTO loan (
    loan_reference_code,
    principal,
    type,
    loan_payment_status,
    disbursement_date,
    disbursement_amount,
    customer_id,
    loan_application_id
) VALUES
    ('LN-10001', 5000.00, 'PERSONAL', 'PENDING', CURRENT_TIMESTAMP, 5000.00, 1, 1),
    ('LN-10002', 7500.00, 'HOME', 'OVERDUE', CURRENT_TIMESTAMP, 7500.00, 2, 2);

-- Repayment Schedules
INSERT INTO repayment_schedule (loan_id) VALUES (1);
INSERT INTO repayment_schedule (loan_id) VALUES (2);

-- Repayment Schedule Entries (ElementCollection table)
-- Assuming Hibernate names it "repayment_schedule_entries" with columns:
-- repayment_schedule_id, entry_due_date, due_date, principal, interest, interest_payment_amount, principal_payment_amount, total_payment_amount, loan_payment_status

INSERT INTO repayment_schedule_entries (
    repayment_schedule_id,
    entry_due_date,
    due_date,
    principal,
    interest,
    interest_payment_amount,
    principal_payment_amount,
    total_payment_amount,
    loan_payment_status
) VALUES
    (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000.00, 200.00, 200.00, 1000.00, 1200.00, 'PENDING'),
    (1, CURRENT_TIMESTAMP + 30, CURRENT_TIMESTAMP + 30, 1000.00, 180.00, 180.00, 1000.00, 1180.00, 'PENDING'),
    (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1500.00, 300.00, 300.00, 1500.00, 1800.00, 'OVERDUE');