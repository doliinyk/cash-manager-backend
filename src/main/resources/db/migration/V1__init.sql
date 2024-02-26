CREATE TABLE users(
    id UUID PRIMARY KEY,
    login VARCHAR(30) NOT NULL,
    password VARCHAR(90) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    account REAL NOT NULL,
    delete_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE expense_categories(
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE users_expense_categories(
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    category_id UUID REFERENCES expense_categories(id) ON DELETE CASCADE
);

CREATE TABLE income_categories (
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE users_income_categories(
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    category_id UUID REFERENCES income_categories(id) ON DELETE CASCADE
);

CREATE TABLE regular_expenses (
    id UUID PRIMARY KEY NOT NULL,
    periodicity BIGINT NOT NULL,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    cost REAL NOT NULL,
    last_payment_date TIMESTAMP WITH TIME ZONE NOT NULL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE single_expenses(
    id UUID PRIMARY KEY NOT NULL,
    category_id UUID REFERENCES expense_categories(id) NOT NULL,
    description VARCHAR(500),
    cost REAL NOT NULL,
    expenses_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE regular_incomes(
    id UUID PRIMARY KEY NOT NULL,
    periodicity bigint NOT NULL,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    profit REAL NOT NULL,
    last_payment_date TIMESTAMP WITH TIME ZONE NOT NULL,
    create_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE single_incomes (
    id UUID PRIMARY KEY NOT NULL,
    category_id UUID REFERENCES income_categories(id) NOT NULL,
    description VARCHAR(500),
    profit REAL NOT NULL,
    income_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL
);