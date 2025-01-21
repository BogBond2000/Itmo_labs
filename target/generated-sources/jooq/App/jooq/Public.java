/*
 * This file is generated by jOOQ.
 */
package App.jooq;


import App.jooq.tables.Dots;
import App.jooq.tables.Users;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.dots</code>.
     */
    public final Dots DOTS = Dots.DOTS;

    /**
     * The table <code>public.users</code>.
     */
    public final Users USERS = Users.USERS;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Arrays.asList(
            Sequences.DOTS_ID_SEQ,
            Sequences.USERS_ID_SEQ
        );
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Dots.DOTS,
            Users.USERS
        );
    }
}
