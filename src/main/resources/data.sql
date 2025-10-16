
-- BOGUS DATA --
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `title`, `pwd_digest`, `pwd_attempts`, `pwd_last_attempted_at`) VALUES
(1, 'bogusUser', 'bogusUser', 'bogus@bogus.com', 'B', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(2, 'testUser', 'testUser', 'normal@testuser.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2007-10-11'),
(3, 'testUser', 'testUser', 'lockout@testuser.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 100, NOW());
--(4, 'testUser', 'testUser', 'absent@testuser.com', 'T', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 100, '2025-10-16 17:47:03')

INSERT INTO `serie` (`id`, `title`, `released_at`) VALUES
(1, 'bogusSerie', NOW()),
(2, 'notWatchedSerie', NOW());

INSERT INTO `episode` (`id`, `serie_id`, `season_nb`, `title`, `duration`, `released_at`) VALUES
(1, 1, 1, 'bogusEpisode', 1, NOW()),
(2, 2, 1, 'notWatchedEpisode', 1, NOW());

INSERT INTO `user_episode_history` (`user_id`, `episode_id`, `watched_at`, `watched_duration`) VALUES
(1, 1, NOW(), 1);

INSERT INTO `user_episode_rating` (`user_id`, `episode_id`, `created_at`, `modified_at`, `rating`) VALUES
(1, 1, NOW(), null, 9999),
(2, 1, NOW(), NOW(), 9999);

INSERT INTO `user_serie_rating` (`user_id`, `serie_id`, `created_at`, `modified_at`, `rating`) VALUES
(1, 1, NOW(), null, 9999),
(2, 1, NOW(), NOW(), 9999);






-- Generation Time: 2025-10-16 17:52:25.0530
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `title`, `pwd_digest`, `pwd_attempts`, `pwd_last_attempted_at`) VALUES
(11, 'Julia', 'Schmidt', 'julia.schmidt@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(12, 'Ethan', 'Miller', 'ethan.miller@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(13, 'Sophie', 'Dubois', 'sophie.dubois@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(14, 'Mateo', 'Rossi', 'mateo.rossi@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(15, 'Olivia', 'Nguyen', 'olivia.nguyen@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(16, 'Lucas', 'Klein', 'lucas.klein@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(17, 'Isabella', 'Garcia', 'isabella.garcia@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(18, 'David', 'Lee', 'david.lee@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(19, 'Emma', 'Novak', 'emma.novak@email.com', 'F', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03'),
(20, 'Noah', 'Peterson', 'noah.peterson@email.com', 'M', '$2a$10$8nadE/niM6bhWleia4HF5.96bb69nwfMV2G./NCS9GSjugi4du5YW', 0, '2025-10-16 17:47:03');

INSERT INTO `serie` (`id`, `title`, `released_at`) VALUES
(11, 'Stranger Things', '2016-07-15'),
(12, 'Breaking Bad', '2008-01-20'),
(13, 'The Witcher', '2019-12-20'),
(14, 'The Mandalorian', '2019-11-12');

INSERT INTO `episode` (`id`, `serie_id`, `season_nb`, `title`, `duration`, `released_at`) VALUES
(11, 11, 1, 'Chapter One: The Vanishing of Will Byers', 49, '2009-07-15'),
(12, 11, 1, 'Chapter Two: The Weirdo on Maple Street', 49, '2009-07-15'),
(13, 11, 1, 'Chapter Three: Holly, Jolly', 49, '2009-07-15'),
(14, 12, 1, 'Pilot', 58, '2008-01-20'),
(15, 12, 1, 'Cat\'s in the Bag...', 48, '2008-01-27'),
(16, 12, 1, '...And the Bag\'s in the River', 48, '2008-02-10'),
(17, 13, 1, 'The End\'s Beginning', 60, '2019-12-20'),
(18, 13, 1, 'Four Marks', 60, '2019-12-20'),
(19, 14, 1, 'Chapter 1: The Mandalorian', 60, '2019-11-12'),
(20, 14, 1, 'Chapter 2: The Child', 60, '2019-11-15'),
(21, 14, 1, 'Chapter 3: The Sin', 60, '2019-11-22');

INSERT INTO `genre` (`id`, `label`) VALUES
(1, 'horror'),
(2, 'action'),
(3, 'adventure'),
(4, 'comedy'),
(5, 'suspense'),
(6, 'sci-fi');

INSERT INTO `serie_genre` (`serie_id`, `genre_id`) VALUES
(11, 1),
(11, 3),
(12, 2),
(12, 4),
(13, 5),
(14, 2),
(14, 3),
(14, 6);

INSERT INTO `user_episode_history` (`user_id`, `episode_id`, `watched_at`, `watched_duration`) VALUES
(11, 12, '2023-05-26', 40),
(11, 17, '2023-08-03', 43),
(11, 21, '2023-11-25', 27),
(12, 13, '2025-08-19', 30),
(12, 14, '2023-08-21', 45),
(12, 15, '2021-05-27', 20),
(12, 17, '2023-04-02', 27),
(12, 20, '2021-12-30', 41),
(13, 16, '2021-09-11', 33),
(13, 18, '2024-04-05', 39),
(13, 19, '2021-11-04', 42),
(13, 21, '2022-07-15', 34),
(14, 11, '2023-11-09', 44),
(14, 12, '2025-07-29', 23),
(14, 14, '2023-09-08', 41),
(14, 16, '2022-10-31', 27),
(14, 19, '2021-10-24', 20),
(14, 21, '2021-07-07', 21),
(15, 19, '2024-04-28', 41),
(16, 12, '2021-03-01', 36),
(16, 13, '2024-11-04', 28),
(16, 14, '2021-01-13', 29),
(16, 17, '2023-01-27', 26),
(16, 21, '2025-08-11', 31),
(17, 11, '2022-01-19', 23),
(17, 12, '2025-02-25', 22),
(17, 13, '2025-05-20', 44),
(17, 15, '2025-02-21', 45),
(17, 16, '2025-08-02', 44),
(17, 17, '2024-10-13', 26),
(17, 21, '2022-11-02', 22),
(18, 12, '2024-01-08', 22),
(18, 13, '2025-09-14', 21),
(18, 20, '2022-04-18', 33),
(19, 19, '2025-10-12', 40),
(19, 20, '2022-12-25', 42),
(20, 11, '2023-06-16', 30),
(20, 12, '2021-02-11', 33),
(20, 18, '2022-04-07', 32);

INSERT INTO `user_episode_rating` (`user_id`, `episode_id`, `created_at`, `modified_at`, `rating`) VALUES
(11, 20, '2021-02-07', NULL, 1),
(11, 21, '2022-08-01', NULL, 1),
(12, 15, '2021-06-05', NULL, 2),
(12, 16, '2021-05-15', NULL, 1),
(12, 17, '2022-06-20', '2023-10-19', 4),
(12, 19, '2021-07-18', NULL, 5),
(12, 21, '2021-02-01', '2023-01-02', 3),
(13, 15, '2022-04-27', '2022-11-01', 5),
(13, 19, '2022-09-06', NULL, 4),
(13, 21, '2021-09-25', NULL, 2),
(14, 13, '2021-07-16', NULL, 2),
(14, 19, '2022-03-18', NULL, 3),
(14, 20, '2021-07-06', '2023-01-24', 2),
(15, 13, '2022-01-13', '2024-09-21', 4),
(15, 14, '2020-11-17', '2024-08-16', 2),
(15, 17, '2022-08-04', '2023-03-07', 1),
(16, 12, '2021-08-13', NULL, 2),
(16, 14, '2021-07-02', NULL, 3),
(16, 18, '2022-09-02', '2024-01-07', 4),
(16, 19, '2022-09-24', NULL, 2),
(16, 20, '2021-09-24', '2023-10-19', 2),
(17, 11, '2022-03-25', NULL, 3),
(17, 12, '2021-05-01', NULL, 3),
(17, 13, '2021-02-26', NULL, 2),
(17, 14, '2022-08-04', NULL, 3),
(17, 15, '2021-03-05', NULL, 1),
(17, 16, '2021-07-25', NULL, 1),
(17, 18, '2020-11-25', NULL, 2),
(17, 20, '2021-08-28', NULL, 1),
(18, 13, '2021-10-16', NULL, 2),
(18, 16, '2022-02-07', NULL, 2),
(19, 12, '2021-01-12', NULL, 4),
(19, 16, '2021-04-23', NULL, 2),
(19, 18, '2022-07-08', NULL, 4),
(19, 20, '2022-02-12', '2023-08-01', 1),
(20, 16, '2020-10-23', NULL, 4),
(20, 17, '2022-08-05', '2024-01-06', 3),
(20, 19, '2022-02-17', NULL, 4),
(20, 20, '2021-06-15', '2023-02-21', 5),
(20, 21, '2022-07-17', NULL, 4);

INSERT INTO `user_serie_rating` (`user_id`, `serie_id`, `created_at`, `modified_at`, `rating`) VALUES
(11, 11, '2021-12-18', '2024-07-16', 5),
(11, 12, '2022-05-14', NULL, 1),
(12, 12, '2022-04-27', NULL, 2),
(13, 14, '2021-05-19', '2023-01-08', 1),
(14, 12, '2021-11-23', NULL, 5),
(14, 13, '2021-01-31', '2024-05-27', 5),
(15, 11, '2022-01-30', NULL, 1),
(16, 14, '2022-10-11', NULL, 2),
(17, 11, '2021-07-04', '2023-10-26', 4),
(17, 14, '2020-11-19', NULL, 2),
(18, 14, '2022-07-16', NULL, 5),
(20, 12, '2021-09-12', NULL, 1),
(20, 13, '2022-01-19', NULL, 4);