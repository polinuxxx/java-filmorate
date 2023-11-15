# java-filmorate

![ER-diagram](src/main/resources/db/er-diagram.png)

## description
ER-diagram for java-filmorate project.

## query examples
### getting film likes
```sql
SELECT COUNT(user_id)
  FROM likes
  WHERE film_id = :id;
```
### getting user friends
```sql
SELECT friend_id
  FROM friends
  WHERE user_id = :id;
```
### getting most popular films
```sql
SELECT film_id,
       COUNT(user_id) AS likes
  FROM likes
  GROUP BY film_id
  ORDER BY likes DESC
  LIMIT :count;
```

