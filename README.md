# java-filmorate
Template repository for Filmorate project.

Схема базы данных:

![alt text](schema.png)

Основные запросы:

Получить всех пользователей:<br>
SELECT * FROM users<br>

Получить всех друзей для пользователя c {id}:<br>
SELECT * FROM users<br>
WHERE id in(<br>
SELECT friend_id<br>
FROM users_friends where user_id = {id})<br>

Получить общих друзей для пользователей c {id1}, {id2}:<br>
SELECT * FROM users<br>
WHERE users.id in<br>
(SELECT u.id<br>
FROM users u<br>
JOIN users_users uf ON u.id = uf.users_id<br>
WHERE uf.users_id = 1 OR uf.users_id = 2<br>
GROUP BY u.id<br>
HAVING count(u.id)=2)<br>

Получить список всех фильмов:<br>
SELECT * FROM films <br>

Получить список всех фильмов с жанром {genre}:<br>
SELECT * FROM films f<br>
JOIN genres_films gf ON f.id = gf.films_id<br>
JOIN genres gs ON gs.id = gf.genre_id<br>
WHERE gs."name" = {genre}<br>

Получить список всех фильмов лайкнутых пользователем с {id}:<br>
SELECT * FROM films f<br>
JOIN likes l ON l.films_id = f.id<br>
JOIN users u ON u.id = l.users_id<br>
WHERE u.id = {id}<br>

Получить 10 самых популярных фильмов:<br>
SELECT * FROM films<br>
WHERE films.id in(<br>
SELECT f.id FROM films f<br>
JOIN likes l ON f.id = l.films_id<br>
GROUP BY f.id<br>
ORDER BY count(f.id)<br>
LIMIT 10)<br>
