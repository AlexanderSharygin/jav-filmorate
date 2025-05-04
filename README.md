# java-filmorate
Template repository for Filmorate project.
Template repository for Filmorate project.

Схема базы данных:
![image](https://github.com/user-attachments/assets/e48aea2f-7395-46a9-8d0b-d5ff97ba50e9)


Основные запросы:

Получить всех польщователей
SELECT *
FROM users

Получить всех друзей для пользователя c {id}
SELECT * FROM users
WHERE id in(
SELECT friend_id
FROM users_friends where user_id = {id})

Получить общих друзей для пользователей c {id1}, {id2}
SELECT * FROM users
WHERE users.id in
(SELECT u.id
FROM users u
JOIN users_users uf ON u.id = uf.users_id
WHERE uf.users_id = 1 OR uf.users_id = 2
GROUP BY u.id
HAVING count(u.id)=2)

Получить cписок всех фильмов
SELECT *
FROM films

Получить cписок всех фильмов с жанорм {genre}
SELECT * FROM films f
JOIN genres_films gf ON f.id = gf.films_id
JOIN genres gs ON gs.id = gf.genre_id
WHERE gs."name" = {genre}

Получить cписок всех фильмов лайкнутых пользоватлем с {id}
SELECT * FROM films f
JOIN likes l ON l.films_id = f.id
JOIN users u ON u.id = l.users_id
WHERE u.id = {id}

Получить 10 самых популярных фильмов
SELECT * FROM films
WHERE films.id in(
SELECT f.id FROM films f
JOIN likes l ON f.id = l.films_id
GROUP BY f.id
ORDER BY count(f.id)
LIMIT 10)
