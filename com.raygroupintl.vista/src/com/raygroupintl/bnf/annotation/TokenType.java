package com.raygroupintl.bnf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.raygroupintl.bnf.Token;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TokenType {
	public Class<? extends Token> value();
}
