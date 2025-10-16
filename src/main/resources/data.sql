
-- $2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW = testPassword
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `title`, `pwd_digest`, `pwd_attempts`, `pwd_last_attempted_at`) VALUES
(1, 'testUser', 'testUser', 'normal@testuser.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2007-10-11'),
(2, 'testUser', 'testUser', 'lockout@testuser.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 100, NOW()),
-- ('testUser', 'testUser', 'absent@email.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 100, NOW());
(10, 'Julia', 'Schmidt', 'julia.schmidt@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(11, 'Ethan', 'Miller', 'ethan.miller@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(12, 'Sophie', 'Dubois', 'sophie.dubois@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(13, 'Mateo', 'Rossi', 'mateo.rossi@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(14, 'Olivia', 'Nguyen', 'olivia.nguyen@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(15, 'Lucas', 'Klein', 'lucas.klein@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(16, 'Isabella', 'Garcia', 'isabella.garcia@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(17, 'David', 'Lee', 'david.lee@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(18, 'Emma', 'Novak', 'emma.novak@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW()),
(19, 'Noah', 'Peterson', 'noah.peterson@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, NOW());

INSERT INTO `serie` (`id`, `title`, `released_at`) VALUES
(1, 'Stranger Things', '2016-07-15'),
(2, 'Breaking Bad', '2008-01-20'),
(3, 'The Witcher', '2019-12-20'),
(4, 'The Mandalorian', '2019-11-12');

INSERT INTO `episode` (`id`, `serie_id`, `season_nb`, `title`, `duration`, `released_at`) VALUES
(1, 1, 1, 'Chapter One: The Vanishing of Will Byers', 49, '2009-07-15'),
(2, 1, 1, 'Chapter Two: The Weirdo on Maple Street',  49, '2009-07-15'),
(3, 1, 1, 'Chapter Three: Holly, Jolly', 49, '2009-07-15'),

(4, 2, 1, 'Pilot', 58, '2008-01-20'),
(5, 2, 1, 'Cat\'s in the Bag...', 48, '2008-01-27'),
(6, 2, 1, '...And the Bag\'s in the River', 48, '2008-02-10'),

(7, 3, 1, 'The End\'s Beginning', 60, '2019-12-20'),
(8, 3, 1, 'Four Marks', 60, '2019-12-20'),

(9, 4, 1, 'Chapter 1: The Mandalorian', 60, '2019-11-12'),
(10, 4, 1, 'Chapter 2: The Child', 60, '2019-11-15'),
(11, 4, 1, 'Chapter 3: The Sin', 60, '2019-11-22');


INSERT INTO `genre` (`id`, `label`) VALUES
(1, 'horror'),
(2, 'action'),
(3, 'adventure'),
(4, 'comedy'),
(5, 'suspense'),
(6, 'sci-fi');

INSERT INTO `serie_genre` (`serie_id`, `genre_id`) VALUES
(1, 1), (1, 3),
(2, 2), (2, 4),
(3, 5),
(4, 2), (4, 3), (4, 6);

INSERT INTO `user_episode_rating` (`user_id`, `episode_id`, `created_at`, `modified_at`, `rating`) VALUES
(10, 10, '2021-02-07', NULL, 1), (10, 11, '2022-08-01', NULL, 1),
(11, 5, '2021-06-05', NULL, 2), (11, 6, '2021-05-15', NULL, 1),(11, 7, '2022-06-20', '2023-10-19', 4), (11, 9, '2021-07-18', NULL, 5),
(11, 11, '2021-02-01', '2023-01-02', 3),
(12, 5, '2022-04-27', '2022-11-01', 5), (12, 9, '2022-09-06', NULL, 4), (12, 11, '2021-09-25', NULL, 2),
(13, 3, '2021-07-16', NULL, 2), (13, 9, '2022-03-18', NULL, 3), (13, 10, '2021-07-06', '2023-01-24', 2),
(14, 3, '2022-01-13', '2024-09-21', 4), (14, 4, '2020-11-17', '2024-08-16', 2), (14, 7, '2022-08-04', '2023-03-07', 1),
(15, 2, '2021-08-13', NULL, 2), (15, 4, '2021-07-02', NULL, 3), (15, 8, '2022-09-02', '2024-01-07', 4), (15, 9, '2022-09-24', NULL, 2), (15, 10, '2021-09-24', '2023-10-19', 2),
(16, 1, '2022-03-25', NULL, 3), (16, 2, '2021-05-01', NULL, 3), (16, 3, '2021-02-26', NULL, 2), (16, 4, '2022-08-04', NULL, 3), (16, 5, '2021-03-05', NULL, 1), (16, 6, '2021-07-25', NULL, 1), (16, 8, '2020-11-25', NULL, 2), (16, 10, '2021-08-28', NULL, 1),
(17, 3, '2021-10-16', NULL, 2), (17, 6, '2022-02-07', NULL, 2),
(18, 2, '2021-01-12', NULL, 4), (18, 6, '2021-04-23', NULL, 2), (18, 8, '2022-07-08', NULL, 4), (18, 10, '2022-02-12', '2023-08-01', 1),
(19, 6, '2020-10-23', NULL, 4), (19, 7, '2022-08-05', '2024-01-06', 3), (19, 9, '2022-02-17', NULL, 4), (19, 10, '2021-06-15', '2023-02-21', 5), (19, 11, '2022-07-17', NULL, 4);

INSERT INTO `user_serie_rating` (`user_id`, `serie_id`, `created_at`, `modified_at`, `rating`) VALUES
(10, 1, '2021-12-18', '2024-07-16', 5), (10, 2, '2022-05-14', NULL, 1),
(11, 2, '2022-04-27', NULL, 2),
(12, 4, '2021-05-19', '2023-01-08', 1),
(13, 2, '2021-11-23', NULL, 5), (13, 3, '2021-01-31', '2024-05-27', 5),
(14, 1, '2022-01-30', NULL, 1),
(15, 4, '2022-10-11', NULL, 2),
(16, 1, '2021-07-04', '2023-10-26', 4), (16, 4, '2020-11-19', NULL, 2),
(17, 4, '2022-07-16', NULL, 5),
(19, 2, '2021-09-12', NULL, 1), (19, 3, '2022-01-19', NULL, 4);

INSERT INTO `user_episode_history` (`user_id`, `episode_id`, `watched_at`, `watched_duration`) VALUES
(10, 2, '2023-05-26', 40), (10, 7, '2023-08-03', 43), (10, 11, '2023-11-25', 27),
(11, 3, '2025-08-19', 30), (11, 4, '2023-08-21', 45), (11, 5, '2021-05-27', 20), (11, 7, '2023-04-02', 27), (11, 10, '2021-12-30', 41),
(12, 6, '2021-09-11', 33), (12, 8, '2024-04-05', 39), (12, 9, '2021-11-04', 42), (12, 11, '2022-07-15', 34),
(13, 1, '2023-11-09', 44), (13, 2, '2025-07-29', 23), (13, 4, '2023-09-08', 41), (13, 6, '2022-10-31', 27), (13, 9, '2021-10-24', 20), (13, 11, '2021-07-07', 21),
(14, 9, '2024-04-28', 41),
(15, 2, '2021-03-01', 36), (15, 3, '2024-11-04', 28), (15, 4, '2021-01-13', 29), (15, 7, '2023-01-27', 26), (15, 11, '2025-08-11', 31),
(16, 1, '2022-01-19', 23), (16, 2, '2025-02-25', 22), (16, 3, '2025-05-20', 44), (16, 5, '2025-02-21', 45), (16, 6, '2025-08-02', 44), (16, 7, '2024-10-13', 26), (16, 11, '2022-11-02', 22),
(17, 2, '2024-01-08', 22), (17, 3, '2025-09-14', 21), (17, 10, '2022-04-18', 33),
(18, 9, '2025-10-12', 40), (18, 10, '2022-12-25', 42),
(19, 1, '2023-06-16', 30), (19, 2, '2021-02-11', 33), (19, 8, '2022-04-07', 32);