package com.hospital.historico.config;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        GraphQLScalarType localDateTimeScalar = GraphQLScalarType.newScalar()
                .name("DateTime") // mantém o nome DateTime do seu schema.graphqls
                .description("Scalar para mapear java.time.LocalDateTime <-> ISO_OFFSET_DATE_TIME")
                .coercing(new Coercing<LocalDateTime, String>() {
                    @Override
                    public String serialize(Object input) throws CoercingSerializeException {
                        if (input instanceof LocalDateTime ldt) {
                            // serializamos como ISO com offset UTC
                            return ldt.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        }
                        throw new CoercingSerializeException("Não foi possível serializar para LocalDateTime: " + input);
                    }

                    @Override
                    public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
                        if (input instanceof String s) {
                            try {
                                // tenta com offset primeiro (ex: 2025-10-04T20:05:00Z)
                                return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
                            } catch (Exception ex) {
                                // fallback para string sem offset
                                return LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
                            }
                        }
                        throw new CoercingParseValueException("Não foi possível converter valor para LocalDateTime: " + input);
                    }

                    @Override
                    public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                        if (input instanceof StringValue sv) {
                            String s = sv.getValue();
                            try {
                                return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
                            } catch (Exception ex) {
                                return LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
                            }
                        }
                        throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was: " +
                                (input == null ? "null" : input.getClass().getSimpleName()));
                    }
                })
                .build();

        return wiringBuilder -> wiringBuilder
                .scalar(localDateTimeScalar)
                .scalar(ExtendedScalars.UUID); // se você ainda precisa do UUID
    }
}
