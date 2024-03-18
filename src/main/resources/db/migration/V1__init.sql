
CREATE TABLE users(
    id UUID PRIMARY KEY,
    login varchar(30) not null,
    password varchar(90) not null,
    email varchar(50) UNIQUE not null,
    create_date timestamp with time zone not null,
    account real not null,
    delete_date timestamp without time zone,
    refresh_token text,
    activated boolean,
    activation_refresh_uuid UUID
);
create unique index login on users using btree (login);
create unique index email on users using btree (email);
CREATE TABLE expense_categories
(
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR (50) NOT NULL
);
CREATE TABLE users_expense_categories(
                                         user_id UUID REFERENCES users(id) ON DELETE CASCADE not null ,
                                         category_id UUID REFERENCES expense_categories(id) ON DELETE CASCADE
);

CREATE TABLE regular_expenses
(
    id UUID PRIMARY KEY not null,
    periodicity bigint not null,
    title VARCHAR (50) NOT NULL,
    description varchar(500),
    cost real not null,
    last_payment_date timestamp with time zone not null,
    create_date timestamp with time zone not null,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null
);
CREATE TABLE single_expenses
(
    id UUID PRIMARY KEY not null,
    category_id UUID REFERENCES expense_categories(id) not null,
    description varchar(500),
    cost real not null,
    expenses_date timestamp with time zone not null ,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null
);
CREATE TABLE income_categories
(
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR (50) NOT NULL
);
CREATE TABLE users_income_categories(
                                        user_id UUID REFERENCES users(id) ON DELETE CASCADE not null,
                                        category_id UUID REFERENCES income_categories(id ) ON DELETE CASCADE
);

CREATE TABLE single_incomes
(
    id UUID PRIMARY KEY not null,
    category_id UUID REFERENCES income_categories(id) not null,
    description varchar(500),
    profit real not null,
    income_date timestamp with time zone not null ,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null
);
CREATE TABLE regular_incomes
(
    id UUID PRIMARY KEY not null,
    periodicity bigint not null,
    title VARCHAR (50) NOT NULL,
    description varchar(500),
    profit real not null,
    last_payment_date timestamp with time zone not null,
    create_date timestamp with time zone not null,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null
);
