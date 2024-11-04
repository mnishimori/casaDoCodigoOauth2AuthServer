create table livro(
	id int auto_increment primary key,
	estante_id int references estante(id),
	titulo varchar(255),
	nota int
);