/*DROP TABLE IF EXISTS unittree_notification */
DROP TABLE IF EXISTS unittree_unit_notification;


/*==============================================================*/
/* Table structure for table unittree_notification				*/
/*==============================================================*/
CREATE TABLE unittree_unit_notification (
	id_unit INT DEFAULT 0 NOT NULL,
	email VARCHAR(255) DEFAULT '' NOT NULL,
	has_notif int(1) NOT NULL default '0',
	use_email int(1) NOT NULL default '0',
	use_list int(1) NOT NULL default '0',
	PRIMARY KEY (id_unit)
);