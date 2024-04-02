INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Kaique', 'kaique@gmail.com', '1140028922', '1998-10-14', '$2a$10$fbvy4mIiu21S5QKHXUYy4OofM6pHda67pVlPGEZWQja3YPLus85kS');
INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Gabriela', 'gabriela@gmail.com', '1140028923', '1998-10-14', '$2a$10$Zpx.Ckoq.98/BHuPnAE/QOhvqxDTF5BNmTrWPavKuh0kMLD7qvhQK');
INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Sunny', 'sunny@gmail.com', '1140028924', '2019-01-05', '$2a$10$fbvy4mIiu21S5QKHXUYy4OofM6pHda67pVlPGEZWQja3YPLus85kS');
INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Thor', 'thorzinho@gmail.com', '1140028925', '2023-04-01', '$2a$10$fbvy4mIiu21S5QKHXUYy4OofM6pHda67pVlPGEZWQja3YPLus85kS');
INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Pandora', 'pandorinha@gmail.com', '1140028926', '2021-06-10', '$2a$10$fbvy4mIiu21S5QKHXUYy4OofM6pHda67pVlPGEZWQja3YPLus85kS');
INSERT INTO tb_user (name, email, phone, birth_date, password) VALUES ('Rex', 'rex@gmail.com', '1140028927', '2010-03-15', '$2a$10$fbvy4mIiu21S5QKHXUYy4OofM6pHda67pVlPGEZWQja3YPLus85kS');

INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Estudar Java', 'Continuar os estudos sobre Java', NULL, NULL, 1, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Projeto pessoal - Do It', 'Finalizar o projeto pessoal Do It (To Do List)', '2023-11-15', '2023-11-30 23:59:59', 1, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Projeto Desenvolvimento Web Faculdade', 'Terminar o e-commerce da aula de Desenvolvimento Web', '2023-08-01 12:00:00', '2023-11-30 23:59:59', 1, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Exercícios físicos', 'Começar a se exercitar diariamente', '2023-11-20', NULL, 2, 'TO_DO');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Matérias do ambiente acadêmico', 'Finalizar as matérias do ambiente acadêmico da faculdade até o prazo', '2023-08-01', '2023-12-04 23:59:59', 1, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Tomar sol', 'Tomar sol diariamente em momentos onde o sol não está tão forte', NULL, NULL, 2, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Ir para a academia', 'Ir para a academia todos os dias da semana', NULL, NULL, 2, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Caminhar com o cachorro', 'Caminhar diariamente com o cachorro', NULL, NULL, 2, 'IN_PROGRESS');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Projeto CLIENT', 'Finalizar o projeto CLIENT API RESTful', '2023-09-15', '2023-10-20', 2, 'DONE');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Estudar para a prova', 'Estudar para a prova de Práticas de Engenharia de Software diariamente usando o conteúdo que o professor disponibiliza no ambiente acadêmico', '2023-01-16 13:00:00', '2023-11-30 16:00:00', 2, 'TO_DO');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Estudar para a prova', 'Estudar para a prova de Práticas Dietéticas', '2023-01-16 13:00:00', '2023-11-30 16:00:00', 2, 'TO_DO');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Montar a diéta', 'Montar a diética com os macro e micro nutrientes necessários', '2023-11-15', '2023-11-23', 2, 'TO_DO');
INSERT INTO tb_task (title, description, start_date, finish_date, priority, task_status) VALUES ('Prova de Engenharia de Software', null, '2023-01-16 19:30:00', '2023-11-30 21:30:00', 2, 'TO_DO');

INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 1);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 2);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 3);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 5);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 8);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 9);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 10);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 13);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 1);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 3);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 5);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 7);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 11);
INSERT INTO tb_user_task (user_id, task_id) VALUES (3, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (4, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (5, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (6, 6);
INSERT INTO tb_user_task (user_id, task_id) VALUES (1, 13);
INSERT INTO tb_user_task (user_id, task_id) VALUES (2, 13);

INSERT INTO tb_category (name) VALUES ('Exercícios');
INSERT INTO tb_category (name) VALUES ('Estudos');
INSERT INTO tb_category (name) VALUES ('Faculdade');
INSERT INTO tb_category (name) VALUES ('Animais domésticos');
INSERT INTO tb_category (name) VALUES ('Cuidados pessoais');
INSERT INTO tb_category (name) VALUES ('Projetos pessoais');

INSERT INTO tb_task_categories(task_id, category_id) VALUES (1, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (2, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (2, 6);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (3, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (3, 3);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (4, 1);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (5, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (5, 3);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (6, 5);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (7, 1);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (7, 5);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (8, 4);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (9, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (9, 6);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (10, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (10, 3);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (11, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (11, 3);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (12, 5);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (13, 2);
INSERT INTO tb_task_categories(task_id, category_id) VALUES (13, 3);

INSERT INTO tb_role(authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role(authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role(role_id, user_id) VALUES (1, 1);
INSERT INTO tb_user_role(role_id, user_id) VALUES (1, 2);
INSERT INTO tb_user_role(role_id, user_id) VALUES (2, 1);

INSERT INTO tb_task_admins(user_id, task_id) VALUES (1, 1);
INSERT INTO tb_task_admins(user_id, task_id) VALUES (1, 3);
INSERT INTO tb_task_admins(user_id, task_id) VALUES (2, 3);
INSERT INTO tb_task_admins(user_id, task_id) VALUES (1, 13);





