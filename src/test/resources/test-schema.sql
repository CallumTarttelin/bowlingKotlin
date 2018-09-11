SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE PLAYER;
TRUNCATE TABLE TEAM_GAME_PLAYERS;
TRUNCATE TABLE GAME;
TRUNCATE TABLE SCORE;
TRUNCATE TABLE LEAGUE;
TRUNCATE TABLE PLAYER_GAME;
TRUNCATE TABLE TEAM;
TRUNCATE TABLE TEAM_GAME;
TRUNCATE TABLE TEAM_PLAYER_GAME;
TRUNCATE TABLE TEAM_PLAYER_GAME_SCORE;

-- start

INSERT INTO PUBLIC.GAME (ID, TIME, VENUE, LEAGUE_ID)
VALUES (12, TIMESTAMP '2018-09-11 21:09:11', 'The Shades', 1),
       (55, TIMESTAMP '2018-09-11 21:09:12', 'Treacle Mine Road', 1),
       (99, TIMESTAMP '2018-09-11 21:10:17', 'Dolly Sisters', 1);

INSERT INTO PUBLIC.LEAGUE (ID, NAME)
VALUES (1, 'City Watch');

INSERT INTO PUBLIC.PLAYER (ID, NAME, TEAM_ID)
VALUES (4, 'Sam Vimes', 2),
       (5, 'Carrot Ironfoundersson', 2),
       (6, 'Nobby Nobbs', 2),
       (7, 'Fred Colon', 2),
       (8, 'Findthee Swing', 3),
       (9, 'Carcer', 3),
       (10, 'Gerald Leastways, a.k.a. Ferret', 3),
       (11, 'Todzy', 3),
       (104, 'Mayonnaise Quirke', 98),
       (105, 'Skully Muldoon', 98),
       (106, 'Doxie', 98);

INSERT INTO PUBLIC.PLAYER_GAME (ID, HANDICAP, GAME_ID, PLAYER_ID)
VALUES (17, 0, 13, 4),
       (18, 0, 13, 5),
       (19, 0, 13, 6),
       (20, 0, 15, 8),
       (21, 0, 15, 9),
       (22, 0, 15, 10),
       (60, 0, 56, 4),
       (61, 0, 56, 7),
       (62, 0, 56, 6),
       (63, 0, 58, 11),
       (64, 0, 58, 9),
       (65, 0, 58, 10),
       (107, 0, 100, 4),
       (108, 0, 100, 5),
       (109, 0, 100, 6),
       (110, 0, 102, 104),
       (111, 0, 102, 105),
       (112, 0, 102, 106);

INSERT INTO PUBLIC.SCORE (ID, HANDICAPPED, SCORE, SCRATCH, TOTAL, PLAYER_GAME_ID)
VALUES (23, 213, 0, 189, FALSE, 18),
       (24, 213, 0, 204, FALSE, 19),
       (25, 363, 2, 297, FALSE, 20),
       (26, 299, 2, 280, FALSE, 22),
       (27, 268, 2, 228, FALSE, 21),
       (28, 275, 2, 262, FALSE, 18),
       (29, 207, 0, 185, FALSE, 22),
       (30, 286, 2, 253, FALSE, 20),
       (31, 225, 2, 181, FALSE, 19),
       (32, 257, 0, 240, FALSE, 21),
       (33, 274, 2, 231, FALSE, 18),
       (34, 266, 2, 246, FALSE, 19),
       (35, 147, 0, 114, FALSE, 21),
       (36, 319, 0, 263, FALSE, 17),
       (37, 187, 0, 166, FALSE, 20),
       (38, 240, 0, 237, FALSE, 17),
       (39, 193, 0, 138, FALSE, 22),
       (40, 283, 2, 238, FALSE, 17),
       (41, 842, 2, 738, TRUE, 17),
       (42, 762, 2, 682, TRUE, 18),
       (43, 704, 2, 631, TRUE, 19),
       (48, 836, 0, 716, TRUE, 20),
       (49, 672, 0, 582, TRUE, 21),
       (50, 699, 0, 603, TRUE, 22),
       (66, 230, 0, 158, FALSE, 61),
       (67, 330, 2, 291, FALSE, 65),
       (68, 258, 2, 239, FALSE, 64),
       (69, 295, 2, 287, FALSE, 64),
       (70, 201, 0, 185, FALSE, 62),
       (71, 306, 2, 236, FALSE, 63),
       (72, 154, 0, 111, FALSE, 60),
       (73, 293, 0, 281, FALSE, 60),
       (74, 322, 2, 267, FALSE, 62),
       (75, 303, 2, 224, FALSE, 63),
       (76, 107, 0, 100, FALSE, 60),
       (77, 177, 0, 142, FALSE, 65),
       (78, 124, 0, 118, FALSE, 65),
       (79, 158, 0, 141, FALSE, 61),
       (80, 310, 2, 298, FALSE, 61),
       (81, 271, 2, 256, FALSE, 62),
       (82, 272, 0, 208, FALSE, 64),
       (83, 180, 2, 123, FALSE, 63),
       (84, 554, 0, 492, TRUE, 60),
       (85, 698, 0, 597, TRUE, 61),
       (86, 794, 2, 708, TRUE, 62),
       (91, 789, 2, 583, TRUE, 63),
       (92, 825, 2, 734, TRUE, 64),
       (93, 631, 0, 551, TRUE, 65);

INSERT INTO PUBLIC.TEAM (ID, NAME, LEAGUE_ID)
VALUES (2, 'The Night Watch', 1),
       (3, 'Cable Street Particulars', 1),
       (98, 'The Day Watch', 1);

INSERT INTO PUBLIC.TEAM_GAME (ID, GAME_ID, TEAM_ID)
VALUES (13, 12, 2),
       (15, 12, 3),
       (56, 55, 2),
       (58, 55, 3),
       (100, 99, 2),
       (102, 99, 98);

INSERT INTO PUBLIC.TEAM_GAME_PLAYERS (TEAM_GAME_ID, PLAYERS_ID)
VALUES (13, 17),
       (13, 18),
       (13, 19),
       (15, 20),
       (15, 21),
       (15, 22),
       (56, 60),
       (56, 61),
       (56, 62),
       (58, 63),
       (58, 64),
       (58, 65),
       (100, 107),
       (100, 108),
       (100, 109),
       (102, 110),
       (102, 111),
       (102, 112);

INSERT INTO PUBLIC.TEAM_PLAYER_GAME (ID, TEAM_GAME_ID)
VALUES (14, 13),
       (16, 15),
       (57, 56),
       (59, 58),
       (101, 100),
       (103, 102);

INSERT INTO PUBLIC.TEAM_PLAYER_GAME_SCORE (ID, HANDICAPPED, SCORE, SCRATCH, TOTAL, TEAM_PLAYER_GAME_ID)
VALUES (44, 745, 0, 656, FALSE, 14),
       (45, 740, 0, 680, FALSE, 14),
       (46, 823, 2, 715, FALSE, 14),
       (47, 2308, 2, 2051, TRUE, 14),
       (51, 930, 2, 805, FALSE, 16),
       (52, 750, 2, 678, FALSE, 16),
       (53, 527, 0, 418, FALSE, 16),
       (54, 2207, 0, 1901, TRUE, 16),
       (87, 585, 0, 454, FALSE, 57),
       (88, 773, 0, 689, FALSE, 57),
       (89, 688, 2, 654, FALSE, 57),
       (90, 2046, 0, 1797, TRUE, 57),
       (94, 894, 2, 766, FALSE, 59),
       (95, 775, 2, 653, FALSE, 59),
       (96, 576, 0, 449, FALSE, 59),
       (97, 2245, 2, 1868, TRUE, 59);

-- end

SET REFERENTIAL_INTEGRITY TRUE;

COMMIT;