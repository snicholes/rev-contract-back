create table associate (
	id serial primary key,
	first_name varchar(80),
	last_name varchar(80),
	secret_code varchar(6) unique not null
);
create table rubric (
	id serial primary key,
	theme varchar(80),
	description varchar(480)
);
create table rubric_value (
	id serial primary key,
	associate_id integer references associate,
	rubric_id integer references rubric,
	score integer check (score>-1 and score <6),
	description varchar(480)
);
create table score (
	id serial primary key,
	week integer,
	note varchar(480),
	rubric_id integer references rubric,
	associate_id integer references associate,
	score_value integer check(score_value>-1)
);
commit;

alter table scores rename to score;
select rubric_value.id, rubric_id, score, rubric_value.description, 
theme from rubric_value join rubric on rubric_value.rubric_id=rubric.id where associate_id=1;