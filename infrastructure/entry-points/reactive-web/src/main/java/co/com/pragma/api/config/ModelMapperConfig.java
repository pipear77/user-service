package co.com.pragma.api.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Conversión explícita de UUID → String
        Converter<UUID, String> uuidToString = ctx ->
                ctx.getSource() == null ? null : ctx.getSource().toString();

        mapper.addConverter(uuidToString);

        // Evita ambigüedades
        mapper.getConfiguration().setAmbiguityIgnored(true);

        return mapper;
    }
}
