create table estante(
	id int auto_increment primary key,
	usuario_id int references usuario(id)
);
