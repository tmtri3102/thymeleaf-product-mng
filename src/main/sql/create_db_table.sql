create database product;
create table product(
    id int primary key not null auto_increment,
    name varchar(100),
    price double,
    description varchar(100),
    manufacturer varchar(100)
);
select * from product;
