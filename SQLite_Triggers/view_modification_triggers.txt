SQLite View Modification Triggers for Movie Rating DB

Author : Ravi Kishor Shakya

1. Write an instead-of trigger that enables updates to the title attribute of view LateRating. 

create trigger tgrUpdateTitle
instead of update of title on LateRating
for each row
when exists(select 1 from Movie where mID = New.mID)
begin
  update Movie set title = New.title
  where mID in (select  mID from Rating 
  where ratingDate > '2011-01-20' 
  and stars = New.stars);
end;

2.Write an instead-of trigger that enables updates to the stars attribute of view LateRating. 

create trigger tgrUpdateStars
instead of update of stars on LateRating
for each row
when exists(select 1 from Rating where mID = New.mID and ratingDate = New.ratingDate)
begin
  update Rating set stars = New.stars
  where mID in (select  mID from Movie where title = New.title)
  and ratingDate > '2011-01-20' ;
end;

3.Write an instead-of trigger that enables updates to the mID attribute of view LateRating.

create trigger tgrUpdateMID
instead of update of mID on LateRating
for each row
begin
  update Movie set mID = New.mID
  where  mID in (select mID from Rating where stars = Old.stars
  and ratingDate > '2011-01-20')
  and mID = Old.mID;
  
  update Rating set mID = New.mID
  where mID in (select mID from Rating where stars = Old.stars
  and ratingDate > '2011-01-20')
  and mID = Old.mID;
end;

4. Write a single instead-of trigger that combines all three of the previous triggers to enable simultaneous updates to attributes mID, title, and/or stars in view LateRating.

create trigger tgrUpdateLateRating
instead of update on LateRating
for each row
when exists(select 1 from Rating where ratingDate = New.ratingDate)
begin
  update Movie set title = New.title
  where mID in (select  mID from Rating 
  where ratingDate > '2011-01-20' 
  and stars >= Old.stars);
  
  update Rating set stars = New.stars
  where stars >= Old.stars
  and ratingDate > '2011-01-20' ;
	
  update Movie set mID = New.mID
  where  mID in (select mID from Rating where stars >=  Old.stars
  and ratingDate > '2011-01-20')
  and mID = Old.mID;
  
  update Rating set mID = New.mID
  where mID in (select mID from Rating where stars >= Old.stars
  and ratingDate > '2011-01-20')
  and mID = Old.mID;
end;

5. Write an instead-of trigger that enables deletions from view HighlyRated. 

create trigger tgrDeleteHighlyRated
instead of delete on HighlyRated
for each row
begin
    delete from Movie
	where mID in (select mID from Rating where stars > 3)
	and mID = Old.mID;
	
	delete from Rating where mID = Old.mID 
	and stars > 3;	
end;

6.Write an instead-of trigger that enables deletions from view HighlyRated.
Policy: Deletions from view HighlyRated should update all ratings for the corresponding movie that have stars > 3 so they have stars = 3. 

create trigger tgrDeleteHighlyRatedUpdate
instead of delete on HighlyRated
for each row
begin
    delete from Movie
	where mID in (select mID from Rating where stars > 3)
	and mID = Old.mID and title = Old.title;
	
	update Rating set stars = 3 where stars > 3
	and mID = Old.mID;	
end;

7. Write an instead-of trigger that enables insertions into view HighlyRated.
Policy: An insertion should be accepted only when the (mID,title) pair already exists
 in the Movie table. (Otherwise, do nothing.) Insertions into view HighlyRated should
 add a new rating for the inserted movie with rID = 201, stars = 5, and NULL 
 ratingDate. 
 
create trigger tgrInsertHighlyRated
instead of insert on HighlyRated
for each row
when exists (select 1 from Movie where mID = New.mID and title = New.title)
begin
    insert into Rating select 201, mID, 5, null from Movie where mID = New.mID and title = New.title;
end;

8. Write an instead-of trigger that enables insertions into view NoRating.
Policy: An insertion should be accepted only when the (mID,title) pair already 
exists in the Movie table. (Otherwise, do nothing.) Insertions into view NoRating 
should delete all ratings for the corresponding movie.

create trigger tgrInsertNoRating
instead of insert on NoRating
for each row
when exists (select 1 from Movie where mID = New.mID and title = New.title)
begin
    delete from Rating where mID = New.mID
end;

9. Write an instead-of trigger that enables deletions from view NoRating.
Policy: Deletions from view NoRating should delete the corresponding 
movie from the Movie table. 

create trigger tgrDeleteNoRating
instead of delete on NoRating
for each row
begin
    delete from Movie where mID = Old.mID and title = Old.title;
end;

10. Write an instead-of trigger that enables deletions from view NoRating.
Policy: Deletions from view NoRating should add a new rating for the deleted 
movie with rID = 201, stars = 1, and NULL ratingDate. 

create trigger tgrDeleteNoRating
instead of delete on NoRating
for each row
begin
    insert into Rating values (201, Old.mID, 1, null);
end;


