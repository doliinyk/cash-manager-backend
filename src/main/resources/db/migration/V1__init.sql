CREATE TABLE users(
                      user_id SERIAL PRIMARY KEY,
                      login varchar(30) not null,
                      password varchar(90) not null,
                      email varchar(50) UNIQUE not null,
                      create_date timestamp with time zone not null,
                      account real not null,
                      delete_date timestamp without time zone
);
CREATE TABLE expense_categories
(
    category_id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR (50) NOT NULL
);
CREATE TABLE users_expense_categories(
                                         user_id INT REFERENCES users(user_id) ON DELETE CASCADE not null ,
                                         category_id INT REFERENCES expense_categories(category_id) ON DELETE CASCADE
);

CREATE TABLE regular_expenses
(
    expenses_id SERIAL PRIMARY KEY not null,
    periodicity bigint not null,
    title VARCHAR (50) NOT NULL,
    description varchar(500),
    cost real not null,
    last_payment_date timestamp with time zone not null,
    create_date timestamp with time zone not null,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE not null
);
CREATE TABLE single_expenses
(
    expenses_id SERIAL PRIMARY KEY not null,
    category_id int REFERENCES expense_categories(category_id) not null,
    description varchar(500),
    cost real not null,
    expenses_date timestamp with time zone not null ,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE not null
);
CREATE TABLE income_categories
(
    category_id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR (50) NOT NULL
);
CREATE TABLE users_income_categories(
                                        user_id INT REFERENCES users(user_id) ON DELETE CASCADE not null,
                                        category_id INT REFERENCES income_categories(category_id ) ON DELETE CASCADE
);

CREATE TABLE single_incomes
(
    income_id SERIAL PRIMARY KEY not null,
    category_id int REFERENCES income_categories(category_id) not null,
    description varchar(500),
    profit real not null,
    income_date timestamp with time zone not null ,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE not null
);
CREATE TABLE regular_incomes
(
    income_id SERIAL PRIMARY KEY not null,
    periodicity bigint not null,
    title VARCHAR (50) NOT NULL,
    description varchar(500),
    cost real not null,
    last_payment_date timestamp with time zone not null,
    create_date timestamp with time zone not null,
    user_id int REFERENCES users(user_id) ON DELETE CASCADE not null
);
