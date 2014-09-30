CREATE TABLE Event(
	id Int PRIMARY KEY,
	imgSrc Varchar(255),
	pubDate Date,
	title Varchar(255),
	description Text
);

CREATE TABLE Info(
	what Varchar(255) PRIMARY KEY,
	data Text
);

CREATE TABLE Inventory (
	id Int PRIMARY KEY,
	price float,
	name Text,
	amount Int
);

CREATE TABLE Dish (
	id Int PRIMARY KEY,
	name Varchar(255),
	priceDiff float
);

CREATE TABLE Dish_has_Inventory (
	dish_id Int,
	inventory_id Int,
	PRIMARY KEY (dish_id, inventory_id),
	FOREIGN KEY (dish_id) REFERENCES Dish(id),
	FOREIGN KEY (inventory_id) REFERENCES Inventory(id)
);

CREATE TABLE Week_menu (
	week Int,
	yyyy Int,
	PRIMARY KEY (yyyy, week)
);
CREATE TABLE Menu_has_Dish (
	menu_id Int,
	dish_id Int,
	PRIMARY KEY (menu_id, dish_id),
	FOREIGN KEY (menu_id) REFERENCES Week_menu(id),
	FOREIGN KEY (dish_id) REFERENCES Dish(id)
);

CREATE TABLE Scheme (
	id int PRIMARY KEY
); 

CREATE TABLE AppUser (
	name Varchar(50),
	pwd Varchar(50),
	scheme int,
	FOREIGN KEY (scheme) REFERENCES Scheme(id)
);

