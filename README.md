# java-filmorate

![ER-diagram](src/main/resources/db/er-diagram.png)

## description
ER-diagram for java-filmorate project.

## team
### team leader :muscle:
@polinuxxx Казичкина Полина
### developers :man_technologist:
@yelgazin Елгазин Максим

@PavelIgK Кистенев Павел

@romilMasnaviev Маснавиев Ромиль

@dmiheev Михеев Дмитрий

## features

- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755534 @romilMasnaviev
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755535 @dmiheev
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755536 @PavelIgK @polinuxxx 
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755538 @yelgazin
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755539 @PavelIgK
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755540 @romilMasnaviev
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755541 @PavelIgK
- [x] https://github.com/users/polinuxxx/projects/2/views/1?pane=issue&itemId=45755542 @polinuxxx

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

