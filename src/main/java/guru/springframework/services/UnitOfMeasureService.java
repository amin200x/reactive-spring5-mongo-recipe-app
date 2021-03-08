package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface UnitOfMeasureService {
    Flux<UnitOfMeasureCommand> listAllUmos();
}
