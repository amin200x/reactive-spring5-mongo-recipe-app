package guru.springframework.converters;

public interface Converter <S, T>{
    T convert(S source);
}
