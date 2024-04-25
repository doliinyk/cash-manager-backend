CREATE TABLE users
(
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

CREATE TABLE expense_categories
(
    id UUID PRIMARY KEY NOT NULL,
    title VARCHAR (50) NOT NULL
);

CREATE TABLE users_expense_categories
(
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null ,
	category_id UUID REFERENCES expense_categories(id),
    color_code text not null
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

CREATE TABLE users_income_categories
(
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE not null,
    category_id UUID REFERENCES income_categories(id ),
    color_code text not null
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


CREATE UNIQUE INDEX users_login_email_idx ON users (login, email);

INSERT INTO expense_categories(id, title)
VALUES ('ed8797f5-7751-46ce-9aee-93ee617b461e', 'health'),
       ('f71afd70-70b2-4a06-830e-bea841ae02be', 'entertainment'),
       ('293bcb88-30b3-433c-b359-1c0507dca184', 'house'),
       ('a18a3d9b-eed1-4de2-ba7a-f6e531a85c91', 'cafe'),
       ('a891eee0-8937-4261-9e3b-9ac0d21c9a41', 'education'),
       ('16bdbc0e-5823-4d5d-b521-704c3af5177d', 'gifts'),
       ('a6a2e85b-0c5e-42a1-983f-dc443e16cc2a', 'family'),
       ('6d447206-327f-450a-8bf3-2c392a0daae9', 'transport'),
       ('d5e93358-db14-4d6d-a0b6-2820f07deb47', 'other');

INSERT INTO income_categories(id, title)
values ('20b6edfa-3e47-4aa7-96b3-6f03d36a1596','salary'),
       ('2b303c2c-aff1-495d-8abb-cebc99762f7d','gift'),
       ('911e2af3-95d3-4112-bb9d-4a3b529562e9','percentages'),
       ('c3b5f88d-88c4-4a43-a84f-3740df7f4aab', 'other');

CREATE OR REPLACE FUNCTION add_categories_to_user() RETURNS TRIGGER AS $add_categories$
BEGIN
    IF (TG_OP = 'INSERT') and  new.id IS not NULL THEN
        begin
            insert into users_expense_categories(id, category_id, user_id, color_code)
            VALUES (gen_random_uuid (), 'ed8797f5-7751-46ce-9aee-93ee617b461e', new.id, '#FF0000'),
                   (gen_random_uuid (), 'f71afd70-70b2-4a06-830e-bea841ae02be', new.id, '#00FF00'),
                   (gen_random_uuid (), '293bcb88-30b3-433c-b359-1c0507dca184', new.id, '#0000FF'),
                   (gen_random_uuid (), 'a18a3d9b-eed1-4de2-ba7a-f6e531a85c91', new.id, '#FFFF00'),
                   (gen_random_uuid (), 'a891eee0-8937-4261-9e3b-9ac0d21c9a41', new.id, '#000080'),
                   (gen_random_uuid (), '16bdbc0e-5823-4d5d-b521-704c3af5177d', new.id, '#008080'),
                   (gen_random_uuid (), 'a6a2e85b-0c5e-42a1-983f-dc443e16cc2a', new.id, '#008000'),
                   (gen_random_uuid (), '6d447206-327f-450a-8bf3-2c392a0daae9', new.id, '#808000'),
                   (gen_random_uuid (), 'd5e93358-db14-4d6d-a0b6-2820f07deb47', new.id, '#808080');
        end;
        BEGIN
        insert into users_income_categories(id, category_id, user_id, color_code)
        values (gen_random_uuid (), '20b6edfa-3e47-4aa7-96b3-6f03d36a1596',new.id,'#00FF00'),
               (gen_random_uuid (), '2b303c2c-aff1-495d-8abb-cebc99762f7d',new.id,'#00FF00'),
               (gen_random_uuid (), '911e2af3-95d3-4112-bb9d-4a3b529562e9',new.id,'#00FF00'),
               (gen_random_uuid (), 'c3b5f88d-88c4-4a43-a84f-3740df7f4aab',new.id,'#00FF00');
        end;
        return new;
    end if;
    RETURN NULL;
END;
$add_categories$ LANGUAGE plpgsql;
CREATE TRIGGER add_categories_to_user
    AFTER INSERT ON users
    FOR EACH ROW EXECUTE PROCEDURE add_categories_to_user();