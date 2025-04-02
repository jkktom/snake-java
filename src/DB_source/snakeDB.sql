CREATE TABLE `users` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `gameResults` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `user_id` integer NOT NULL,
  `food1Score` integer DEFAULT 0,
  `food2Score` integer DEFAULT 0,
  `obstacleCount` integer DEFAULT 0,
  `gameDuration` integer NOT NULL,
  `endTime` timestamp NOT NULL,
  `wasAIEnabled` boolean DEFAULT false
);

CREATE TABLE `comments` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `game_result_id` integer NOT NULL,
  `user_id` integer NOT NULL,
  `content` text NOT NULL
);

CREATE INDEX `idx_user_games` ON `gameResults` (`user_id`);

ALTER TABLE `gameResults` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`game_result_id`) REFERENCES `gameResults` (`id`);

ALTER TABLE `comments` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);