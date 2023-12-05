# java-filmorate

![ER-diagram](src/main/resources/db/er-diagram.png)

ER-diagram for java-filmorate project.

## description
Java-filmorate application allows users to rate films, 
leave reviews and give feedback on them, 
friend members and watch news feed

## team
### team leader :muscle:
@polinuxxx Казичкина Полина
### developers :man_technologist:
@yelgazin Елгазин Максим

@PavelIgK Кистенев Павел

@romilMasnaviev Маснавиев Ромиль

@dmiheev Михеев Дмитрий

## features

- [x] https://github.com/polinuxxx/java-filmorate/issues/26 @polinuxxx
- [x] https://github.com/polinuxxx/java-filmorate/issues/28 @dmiheev
- [x] https://github.com/polinuxxx/java-filmorate/issues/29 @PavelIgK
- [x] https://github.com/polinuxxx/java-filmorate/issues/30 @yelgazin
- [x] https://github.com/polinuxxx/java-filmorate/issues/31 @PavelIgK @polinuxxx
- [x] https://github.com/polinuxxx/java-filmorate/issues/32 @romilMasnaviev
- [x] https://github.com/polinuxxx/java-filmorate/issues/33 @romilMasnaviev
- [x] https://github.com/polinuxxx/java-filmorate/issues/34 @PavelIgK

## query examples
### getting film likes
```sql
SELECT COUNT(user_id)
  FROM film_marks
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
       AVG(mark) AS rate
  FROM film_marks
  GROUP BY film_id
  ORDER BY rate DESC
  LIMIT :count;
```

