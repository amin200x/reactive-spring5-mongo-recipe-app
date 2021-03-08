package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositores.UnitOfMeasureRepository;
import guru.springframework.repositores.reactive.UnitOgMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOgMeasureReactiveRepository repository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureCommandToUnitOfMeasure;

    public UnitOfMeasureServiceImpl(UnitOgMeasureReactiveRepository repository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureCommandToUnitOfMeasure) {
        this.repository = repository;
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUmos() {
        return repository
                .findAll()
                .map(unitOfMeasureCommandToUnitOfMeasure::convert);
       /* return StreamSupport.stream(repository.findAll()
                .spliterator(), false)
                .map(unitOfMeasureCommandToUnitOfMeasure::convert)
                .collect(Collectors.toSet());*/
    }
}
