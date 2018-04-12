# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table clazz (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_clazz primary key (id)
);

create table mark (
  id                            integer auto_increment not null,
  subject_id                    bigint,
  student_id                    bigint,
  value                         double not null,
  constraint pk_mark primary key (id)
);

create table security_role (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_security_role primary key (id)
);

create table student (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  clazz_id                      bigint,
  constraint uq_student_user_id unique (user_id),
  constraint pk_student primary key (id)
);

create table subject (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_subject primary key (id)
);

create table subject_clazz (
  subject_id                    bigint not null,
  clazz_id                      bigint not null,
  constraint pk_subject_clazz primary key (subject_id,clazz_id)
);

create table teacher (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  subject_id                    bigint,
  constraint uq_teacher_user_id unique (user_id),
  constraint pk_teacher primary key (id)
);

create table teacher_clazz (
  teacher_id                    bigint not null,
  clazz_id                      bigint not null,
  constraint pk_teacher_clazz primary key (teacher_id,clazz_id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  enabled                       tinyint default false not null,
  firstname                     varchar(255),
  lastname                      varchar(255),
  student_id                    bigint,
  teacher_id                    bigint,
  constraint uq_user_student_id unique (student_id),
  constraint uq_user_teacher_id unique (teacher_id),
  constraint pk_user primary key (id)
);

create table user_security_role (
  user_id                       bigint not null,
  security_role_id              bigint not null,
  constraint pk_user_security_role primary key (user_id,security_role_id)
);

create table user_user_permission (
  user_id                       bigint not null,
  user_permission_id            bigint not null,
  constraint pk_user_user_permission primary key (user_id,user_permission_id)
);

create table user_permission (
  id                            bigint auto_increment not null,
  permission_value              varchar(255),
  constraint pk_user_permission primary key (id)
);

alter table mark add constraint fk_mark_subject_id foreign key (subject_id) references subject (id) on delete restrict on update restrict;
create index ix_mark_subject_id on mark (subject_id);

alter table mark add constraint fk_mark_student_id foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_mark_student_id on mark (student_id);

alter table student add constraint fk_student_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table student add constraint fk_student_clazz_id foreign key (clazz_id) references clazz (id) on delete restrict on update restrict;
create index ix_student_clazz_id on student (clazz_id);

alter table subject_clazz add constraint fk_subject_clazz_subject foreign key (subject_id) references subject (id) on delete restrict on update restrict;
create index ix_subject_clazz_subject on subject_clazz (subject_id);

alter table subject_clazz add constraint fk_subject_clazz_clazz foreign key (clazz_id) references clazz (id) on delete restrict on update restrict;
create index ix_subject_clazz_clazz on subject_clazz (clazz_id);

alter table teacher add constraint fk_teacher_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table teacher add constraint fk_teacher_subject_id foreign key (subject_id) references subject (id) on delete restrict on update restrict;
create index ix_teacher_subject_id on teacher (subject_id);

alter table teacher_clazz add constraint fk_teacher_clazz_teacher foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;
create index ix_teacher_clazz_teacher on teacher_clazz (teacher_id);

alter table teacher_clazz add constraint fk_teacher_clazz_clazz foreign key (clazz_id) references clazz (id) on delete restrict on update restrict;
create index ix_teacher_clazz_clazz on teacher_clazz (clazz_id);

alter table user add constraint fk_user_student_id foreign key (student_id) references student (id) on delete restrict on update restrict;

alter table user add constraint fk_user_teacher_id foreign key (teacher_id) references teacher (id) on delete restrict on update restrict;

alter table user_security_role add constraint fk_user_security_role_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_security_role_user on user_security_role (user_id);

alter table user_security_role add constraint fk_user_security_role_security_role foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;
create index ix_user_security_role_security_role on user_security_role (security_role_id);

alter table user_user_permission add constraint fk_user_user_permission_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_user_permission_user on user_user_permission (user_id);

alter table user_user_permission add constraint fk_user_user_permission_user_permission foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;
create index ix_user_user_permission_user_permission on user_user_permission (user_permission_id);


# --- !Downs

alter table mark drop foreign key fk_mark_subject_id;
drop index ix_mark_subject_id on mark;

alter table mark drop foreign key fk_mark_student_id;
drop index ix_mark_student_id on mark;

alter table student drop foreign key fk_student_user_id;

alter table student drop foreign key fk_student_clazz_id;
drop index ix_student_clazz_id on student;

alter table subject_clazz drop foreign key fk_subject_clazz_subject;
drop index ix_subject_clazz_subject on subject_clazz;

alter table subject_clazz drop foreign key fk_subject_clazz_clazz;
drop index ix_subject_clazz_clazz on subject_clazz;

alter table teacher drop foreign key fk_teacher_user_id;

alter table teacher drop foreign key fk_teacher_subject_id;
drop index ix_teacher_subject_id on teacher;

alter table teacher_clazz drop foreign key fk_teacher_clazz_teacher;
drop index ix_teacher_clazz_teacher on teacher_clazz;

alter table teacher_clazz drop foreign key fk_teacher_clazz_clazz;
drop index ix_teacher_clazz_clazz on teacher_clazz;

alter table user drop foreign key fk_user_student_id;

alter table user drop foreign key fk_user_teacher_id;

alter table user_security_role drop foreign key fk_user_security_role_user;
drop index ix_user_security_role_user on user_security_role;

alter table user_security_role drop foreign key fk_user_security_role_security_role;
drop index ix_user_security_role_security_role on user_security_role;

alter table user_user_permission drop foreign key fk_user_user_permission_user;
drop index ix_user_user_permission_user on user_user_permission;

alter table user_user_permission drop foreign key fk_user_user_permission_user_permission;
drop index ix_user_user_permission_user_permission on user_user_permission;

drop table if exists clazz;

drop table if exists mark;

drop table if exists security_role;

drop table if exists student;

drop table if exists subject;

drop table if exists subject_clazz;

drop table if exists teacher;

drop table if exists teacher_clazz;

drop table if exists user;

drop table if exists user_security_role;

drop table if exists user_user_permission;

drop table if exists user_permission;

