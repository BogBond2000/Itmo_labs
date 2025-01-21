/*
 * This file is generated by jOOQ.
 */
package App.jooq;


import App.jooq.tables.Dots;
import App.jooq.tables.Users;
import App.jooq.tables.records.DotsRecord;
import App.jooq.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<DotsRecord> DOTS_PKEY = Internal.createUniqueKey(Dots.DOTS, DSL.name("dots_pkey"), new TableField[] { Dots.DOTS.ID }, true);
    public static final UniqueKey<UsersRecord> USERS_PKEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_pkey"), new TableField[] { Users.USERS.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<DotsRecord, UsersRecord> DOTS__DOTS_IDUSER_FKEY = Internal.createForeignKey(Dots.DOTS, DSL.name("dots_iduser_fkey"), new TableField[] { Dots.DOTS.IDUSER }, Keys.USERS_PKEY, new TableField[] { Users.USERS.ID }, true);
}
