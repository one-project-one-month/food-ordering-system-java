package org._p1m.foodorderingsystem.common.jpa;

import org._p1m.foodorderingsystem.common.util.PluralizeUtil;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PluralPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
        if (name == null) return null;

        final String snakeCase = PluralizeUtil.toSnakeCase(name.getText());
        final String tableName = PluralizeUtil.pluralizeSnakeCase(snakeCase);

        return Identifier.toIdentifier(tableName);
    }
}
