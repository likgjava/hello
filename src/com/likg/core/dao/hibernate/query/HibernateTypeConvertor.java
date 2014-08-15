package com.likg.core.dao.hibernate.query;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.type.Type;


/**
 * @author <a href="mailto:scorpio_leon@hotmail.com">Leon Li</a>
 *
 */
public class HibernateTypeConvertor {
    
	/*
	 * 不可实例化。
	 */
    private HibernateTypeConvertor() {
    }

    /**
     * 根据SQL的类型获得Hibernate对应的类型。
     * @param sqlType int
     * @return Type
     * @throws HibernateException
     */
    @SuppressWarnings("deprecation")
	public static Type getTypeBySqlType(int sqlType) throws HibernateException {
        Type type = null;

        switch (sqlType) {
        case Types.BIGINT:
            type = Hibernate.LONG;
            break;
        case Types.BOOLEAN:
            type = Hibernate.BOOLEAN;
            break;
        case Types.CHAR:
            type = Hibernate.CHARACTER;
            break;
        case Types.DATE:
            type = Hibernate.DATE;
            break;
        case Types.DOUBLE:
            type = Hibernate.DOUBLE;
            break;
        case Types.INTEGER:
            type = Hibernate.INTEGER;
            break;
        case Types.JAVA_OBJECT:
            type = Hibernate.OBJECT;
            break;
        case Types.LONGVARCHAR:
            type = Hibernate.TEXT;
            break;
        case Types.TIME:
            type = Hibernate.TIME;
            break;
        case Types.TIMESTAMP:
            type = Hibernate.TIMESTAMP;
            break;
        case Types.VARCHAR:
            type = Hibernate.STRING;
            break;
        default:
            throw new HibernateException("Unsupported hibernate sql type: " + sqlType);
        }
        return type;
    }
    
}
